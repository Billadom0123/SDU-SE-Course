package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Memory;
import org.fatmansoft.teach.models.Person;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.MemoryRepository;
import org.fatmansoft.teach.repository.PersonRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lyh")
public class LyhController {
    @Value("${attach.folder}")
    private String attachFolder;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;


    //找到当前学生
    public Student getCurrentStudent(){
        return studentRepository.findStudentByUserId(getCurrentUser()).get();
    }

    public Integer getCurrentUser(){
        return CommonMethod.getUserId();
    }

    public Person getCurrentPerson(){
        Optional<User> byUserId = userRepository.findByUserId(getCurrentUser());
        Person person = byUserId.get().getPerson();
        return person;
    }

    public String getMemoryImageString(String attachFolder, String id) {
        String fileName =attachFolder + "MemoryImage/" + getCurrentUser()+"-"+id  + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUserBackGroundImageString(String attachFolder) {
        String fileName =attachFolder + "LyhUserBackGround/" + getCurrentUser()+"-"+"current"  + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDefaultJPGImageString(String attachFolder, String id) {
        String fileName =attachFolder + "LyhUserBackGround/" +"default"+ id  + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDefaultPNGImageString(String attachFolder, String id) {
        String fileName =attachFolder + "LyhUserBackGround/" +"default"+ id  + ".png";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUserPreviousBackGroundString(String attachFolder, Integer id){
        String fileName =attachFolder + "LyhUserBackGround/" +getCurrentUser()+ "-"+ id + "-" + "previous" + ".JPG";;
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //图片版的getMaxId()
    public Integer getUserPreviousBackGroundMaxIndex(String attachFolder) {
        String fileName =attachFolder + "LyhUserBackGround/" +getCurrentUser()+ "-"+ "1" + "-" + "previous" + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return 0;
        else{
            Integer index =1;
            while(file.exists()){
                file=new File(attachFolder + "LyhUserBackGround/" +getCurrentUser()+ "-"+ (index+1) + "-" + "previous" + ".JPG");
                index++;
            }
            return (index-1);
        }
    }

    //判断用户历史壁纸是否有已经选中的这一张
    public boolean containNewBackGround(Integer maxId,Integer index,String suffix){
        boolean flag=false;
        String imageUrl = "";
        if(Objects.equals(suffix, "JPG")){
            imageUrl=getDefaultJPGImageString(attachFolder,index.toString());
        }
        else{
            imageUrl=getDefaultPNGImageString(attachFolder,index.toString());
        }
        for(Integer i=1;i<=maxId;i++){
            if(Objects.equals(getUserPreviousBackGroundString(attachFolder, i), imageUrl)){
                flag=true;
                break;
            }
        }
        return flag;
    }

    public String getPreviousBackGroundString(String attachFolder,String id){
        String fileName =attachFolder + "LyhUserBackGround/" +getCurrentUser()+"-"+ id+"-previous"  + ".JPG";
        File file = new File(fileName);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            int size = (int) file.length();
            byte data[] = new byte[size];
            in.read(data);
            in.close();
            String imgStr = "data:image/png;base64,";
            String s = new String(Base64.getEncoder().encode(data));
            imgStr = imgStr + s;
            return imgStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @PostMapping("/getLyhPerson")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getLyhPerson(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList<>();
        String introduce = getCurrentStudent().getIntroduce();
        Integer userId = getCurrentUser();
        String userName = userRepository.findByUserId(userId).get().getUserName();
        Map m = new HashMap<>();
        m.put("username",userName);
        m.put("introduce",introduce);
        dataList.add(m);
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/getMemoryImage")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getMemoryImage(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList<>();
        List<Memory> memoryByUserId = memoryRepository.findMemoryByUserId(getCurrentUser());
        for (Memory memory : memoryByUserId) {
            String memoryImageString = getMemoryImageString(attachFolder, memory.getId().toString());
            Map m = new HashMap();
            m.put("id",memory.getId());
            m.put("content",memory.getMemory());
            m.put("imageUrl",memoryImageString);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/uploadMemoryImage")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse uploadPersonImage(@RequestParam Map pars, @RequestParam("file") MultipartFile file) {
        String content = CommonMethod.getString(pars,"content");
        Integer maxId = memoryRepository.getMaxId()==null?1: memoryRepository.getMaxId();
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "MemoryImage/" + getCurrentUser() + "-" + (maxId+1) + ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Memory memory = new Memory();
        memory.setId(maxId+1);
        memory.setMemory(content);
        memory.setUser(userRepository.getById(getCurrentUser()));
        memoryRepository.save(memory);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteMemoryImage")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse deleteMemoryImage(@Valid @RequestBody DataRequest dataRequest) {
        Map data = dataRequest.getData();
        Integer ImageId = (Integer) data.get("id");
        String fileName =attachFolder + "MemoryImage/" + getCurrentUser()+"-"+ ImageId + ".JPG";
        File file = new File(fileName);
        file.delete();
        memoryRepository.deleteById(ImageId);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getUserBackGround")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getUserBackGround(@Valid @RequestBody DataRequest dataRequest) {
        String userBackGroundImageString = getUserBackGroundImageString(attachFolder);
        return CommonMethod.getReturnData(userBackGroundImageString);
    }

    @PostMapping("/getDefaultBackGround")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getDefaultBackGround(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        String jpg = getDefaultJPGImageString(attachFolder,"1");
        String png = getDefaultPNGImageString(attachFolder,"1");
        Integer i = 1;
        //当两个中有一个存在 一般不会有人一样的图片jpg来一张,png来一张
        while(!(Objects.equals(jpg, "") && Objects.equals(png, ""))){
            //获取那个存在的
            String imageUrl = Objects.equals(jpg, "") ?png:jpg;
            Map m = new HashMap();
            m.put("imageUrl",imageUrl);
            m.put("index",i);
            //放后缀
            m.put("suffix", Objects.equals(jpg, "") ?"png":"JPG");
            m.put("selected", Objects.equals(imageUrl, getUserBackGroundImageString(attachFolder)) ?"true":"false");
            dataList.add(m);
            //迭代
            jpg=getDefaultJPGImageString(attachFolder,(++i).toString());
            png=getDefaultPNGImageString(attachFolder,(i).toString());
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/setUserBackGroundInDefault")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse setUserBackGroundInDefault(@Valid @RequestBody DataRequest dataRequest) {
        Map d = dataRequest.getData();
        Integer index = (Integer) d.get("index");
        String suffix = (String) d.get("suffix");
        try{
            FileInputStream fileInputStream = new FileInputStream(attachFolder + "LyhUserBackGround/" + "default" + index + "." + suffix);
            File file = new File(attachFolder + "LyhUserBackGround/" + "default" + index + "."+suffix);
//            int size = (int)file.getSize();
            int size = (int) file.length();
            byte [] data = new byte[size];
            fileInputStream.read(data);
            fileInputStream.close();
            String fileName =attachFolder + "LyhUserBackGround/" + getCurrentUser() + "-" +"current" + ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String imageUrl="";
        if(Objects.equals(suffix, "JPG")) {
            imageUrl = getDefaultJPGImageString(attachFolder, index.toString());
        }
        else{
            imageUrl=getDefaultPNGImageString(attachFolder,index.toString());
        }
        return CommonMethod.getReturnData(imageUrl);
    }

    @PostMapping("/addUserPreviousBackGroundFromDefault")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse addUserPreviousBackGroundFromDefault(@Valid @RequestBody DataRequest dataRequest){
        Map d = dataRequest.getData();
        //获取用户历史壁纸中Id最大的那个
        Integer maxId = getUserPreviousBackGroundMaxIndex(attachFolder);
        Integer index = (Integer) d.get("index");
        String suffix = (String) d.get("suffix");
        try{
            //第一步 检查当前选中的是否已经存在于历史壁纸了
            //获取这个默认壁纸
            File file = new File(attachFolder + "LyhUserBackGround/" + "default" + index + "."+suffix);
            //获得他的imageUrl,和1到maxId的所有图片的imageUrl对比，如果有相同，则不存
            if(!containNewBackGround(maxId,index,suffix)){
                FileInputStream fileInputStream = new FileInputStream(attachFolder + "LyhUserBackGround/" + "default" + index + "." + suffix);
                int size = (int) file.length();
                byte [] data = new byte[size];
                fileInputStream.read(data);
                fileInputStream.close();
                String fileName =attachFolder + "LyhUserBackGround/" +getCurrentUser()+ "-"+ (maxId+1) + "-" + "previous" + ".JPG";
                FileOutputStream out = new FileOutputStream(fileName);
                out.write(data);
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getPreviousBackGround")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse getPreviousBackGround(@Valid @RequestBody DataRequest dataRequest){
        List dataList = new ArrayList();
        String userPreviousBackGroundString = getUserPreviousBackGroundString(attachFolder, 1);
        Integer i=1;
        while(!Objects.equals(userPreviousBackGroundString, "")){
            String imageUrl = "";
            imageUrl = getUserPreviousBackGroundString(attachFolder,i);
            Map m = new HashMap();
            m.put("imageUrl",imageUrl);
            m.put("index",i);
            m.put("selected", Objects.equals(imageUrl, getUserBackGroundImageString(attachFolder)) ?"true":"false");
            dataList.add(m);
            userPreviousBackGroundString=getUserPreviousBackGroundString(attachFolder,++i);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/setUserBackGroundInPrevious")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse setUserBackGroundInPrevious(@Valid @RequestBody DataRequest dataRequest){
        Map d = dataRequest.getData();
        Integer index = (Integer) d.get("index");
        try{
            FileInputStream fileInputStream = new FileInputStream(attachFolder + "LyhUserBackGround/" + getCurrentUser()+"-"+index+"-previous.JPG");
            File file = new File(attachFolder + "LyhUserBackGround/" + getCurrentUser()+"-"+index+"-previous.JPG");
            int size = (int) file.length();
            byte [] data = new byte[size];
            fileInputStream.read(data);
            fileInputStream.close();
            String fileName =attachFolder + "LyhUserBackGround/" + getCurrentUser() + "-" +"current" + ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String imageUrl = getUserBackGroundImageString(attachFolder);
        return CommonMethod.getReturnData(imageUrl);
    }

    @PostMapping("/setUserBackGroundInNew")
    @PreAuthorize("hasRole('STUDENT')")
    public DataResponse setUserBackGroundInNew(@RequestParam Map pars, @RequestParam("file") MultipartFile file){
        Integer maxId = getUserPreviousBackGroundMaxIndex(attachFolder);
        //先把这张写进曾经选用去
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "LyhUserBackGround/" + getCurrentUser() + "-" + (maxId+1) +"-previous"+ ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //再把这张换成当前的用户壁纸
        try{
            InputStream in = file.getInputStream();
            int size = (int)file.getSize();
            byte [] data = new byte[size];
            in.read(data);
            in.close();
            String fileName =attachFolder + "LyhUserBackGround/" + getCurrentUser() + "-current"+ ".JPG";
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.getReturnMessageOK();
    }



}
