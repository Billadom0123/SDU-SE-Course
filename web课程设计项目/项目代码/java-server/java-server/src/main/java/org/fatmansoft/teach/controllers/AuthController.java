package org.fatmansoft.teach.controllers;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.fatmansoft.teach.models.EUserType;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.UserType;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.request.EmailRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
import org.fatmansoft.teach.util.CheckCodeUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.UimsUtil;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.fatmansoft.teach.payload.request.LoginRequest;
import org.fatmansoft.teach.payload.request.SignupRequest;
import org.fatmansoft.teach.payload.response.JwtResponse;
import org.fatmansoft.teach.payload.response.MessageResponse;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.security.jwt.JwtUtils;
import org.fatmansoft.teach.security.services.UserDetailsImpl;
import org.yaml.snakeyaml.Yaml;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static String correctCheckCodeString;

    private final String[] tempPass = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",""};
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private JavaMailSender javaMailSender;



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getUserId(),
                roles.get(0)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Integer maxId = userRepository.getMaxId();
        user.setId(++maxId);
        Set<String> strRoles = signUpRequest.getRole();

        if (strRoles == null) {
            UserType userRole = userTypeRepository.findByName(EUserType.ROLE_STUDENT);
            user.setUserType(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserType adminRole = userTypeRepository.findByName(EUserType.ROLE_ADMIN);
                        user.setUserType(adminRole);

                        break;
                    case "teacher":
                        UserType teacherRole = userTypeRepository.findByName(EUserType.ROLE_TEACHER);
                        user.setUserType(teacherRole);
                        break;
                    default:
                        UserType userRole = userTypeRepository.findByName(EUserType.ROLE_STUDENT);
                        user.setUserType(userRole);
                }
            });
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/getUimsConfig")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getUimsConfig(@Valid @RequestBody DataRequest dataRequest) {
        Map data = new HashMap();;
        InputStream in = null;
        try {
            Yaml yaml = new Yaml();
            Resource resource = resourceLoader.getResource("classpath:uims.yml");
            in = resource.getInputStream();
            data =(Map)yaml.load(in);
        }catch(Exception e){

        }
        return CommonMethod.getReturnData(data);
    }

    @PostMapping("/sendEmail")
    public DataResponse sendEmail(@Valid @RequestBody DataRequest dataRequest,HttpServletRequest request) {
        String username = (String) dataRequest.getData().get("username");
        String checkCodeString =(String) dataRequest.getData().get("checkCodeString");
//        HttpSession session = request.getSession();
//        String checkCode = (String) session.getAttribute("CheckCode");
//        System.out.println(correctCheckCodeString);
        if(!correctCheckCodeString.equals(checkCodeString)){
            return CommonMethod.getReturnMessageError("验证码错误");
        }
        if(!userRepository.findByUserName(username).isPresent()){
            return CommonMethod.getReturnMessageError("该账号不存在");
        }
        User user = userRepository.findByUserName(username).get();
        String email = user.getPerson().getEmail();
        if(email==null){
            return CommonMethod.getReturnMessageError("该账号没有绑定邮箱!");
        }
        //1.创建一个邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //2.发送邮件的一个工具类
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            Random random = new Random();
            String temp="";
            for(int i=1;i<=8;i++){
                int j = random.nextInt(36);
                temp+=tempPass[j];
            }
            user.setPassword(encoder.encode(temp));
            userRepository.save(user);
            //获得邮件标题
            mimeMessageHelper.setSubject("沙袋人员信息管理系统密码重置");
            //获得邮件内容
            mimeMessageHelper.setText("检测到您已经通过验证,暂行密码:"+temp+"<br/>"+"from:我杀了一只猩猩", true);
            //获得发件人名字
            mimeMessageHelper.setFrom("3054117680@qq.com");
            //收件人
            mimeMessageHelper.setTo(email);
            //发送邮件
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

    @RequestMapping("/generateCheckCode")
    public void generateCheckCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
//        Cookie c = new Cookie("1","1");
//        c.setMaxAge(7*24*60*60);
//        response.addCookie(c);
        ServletOutputStream outputStream = response.getOutputStream();

        String CheckCode = CheckCodeUtil.outputVerifyImage(100,50,outputStream,4);


        session.setAttribute("CheckCode",CheckCode);
        Cookie cookie = new Cookie("CheckCode",CheckCode);
        int seconds=7*24*60*60;//Cookie保存时间
        cookie.setMaxAge(seconds);
        response.addCookie(cookie);
        correctCheckCodeString=CheckCode;
//        return CommonMethod.getReturnMessage("0",CheckCode);
    }

    //自动在本地创建文件夹
    @PostMapping("/generateDirectories")
    public DataResponse generateDirectories(@Valid @RequestBody DataRequest dataRequest){
        File file = new File("C:\\target");
        file.mkdir();
        file= new File("C:\\target\\CourseZip");
        file.mkdir();
        file= new File("C:\\target\\LyhUserBackGround");
        file.mkdir();
        file= new File("C:\\target\\MemoryImage");
        file.mkdir();
        file= new File("C:\\target\\userAvatar");
        file.mkdir();
        file= new File("C:\\target\\userProof");
        file.mkdir();
        file= new File("C:\\target\\WordCloud");
        file.mkdir();
        return CommonMethod.getReturnMessageOK();
    }
}
