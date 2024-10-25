*** Settings ***
Library           SeleniumLibrary
Library           RequestsLibrary

*** Keywords ***
login
    [Arguments]    ${uid}    ${pwd}
    # 打开浏览器并加载网页
    Open Browser    ${login}    ${ff}
    # 输入用户名密码
    Input Text    id = username    ${uid}
    Input Password    id = password    ${pwd}
    # 点击登录按钮
    Click Button    name = submit
    # 检查登录是否成功
    ${alert}    Run Keyword And Return Status    Execute Javascript    return !!window.alert
    Run Keyword If    ${alert}    Log    用户${uid}登录成功
    ...    ELSE    Log    用户${uid}登陆失败
    &{cookies}    Get Cookies    True    # 以字典形式获取cookie
    Return From Keyword    &{cookies}

submit code
    [Arguments]    ${id}    ${lang}    ${source}    &{cookies}
    Create Session    oj    ${ip}    \    ${cookies}
    Log    oj
    ${payload}=    Create Dictionary    id=${id}    language=${lang}    source=${source}
    ${response}=    POST On Session    oj    /submit.php    data=${payload}
    Log    ${response.content}
    Status Should Be    200    # 应该得到正确响应

check submit result
    [Arguments]    ${username}    ${should_be_result}
    # 检查结果
    ${uStatus}    Set Variable    ${status}?user_id=${username}
    Go To    ${uStatus}
    Sleep    3    # 3s内测评必定结束
    @{tds}    Get WebElements    class = label
    ${res}    Set Variable    ${tds}[0]
    ${result}    Get Text    ${res}
    Log    代码评测结果为${result}
    Should Start With    ${result}    ${should_be_result}

register
    [Arguments]    ${uid}    ${name}    ${pwd}    ${rpt}    ${school}    ${email}    ${should_contain}
    [Tags]    PostRegister
    ${payload}=    Create Dictionary    user_id=${uid}    nick=${name}    password=${pwd}    rptpassword=${rpt}    school=${school}    email=${email}
    ${response}=    POST    ${ip}/register.php    data=${payload}
    Log    ${response.content}
    Status Should Be    200    # 应该得到正确响应
    ${bytes_seq}    Evaluate    '${should_contain}'.encode('utf-8')
    Should Contain    ${response.content}    ${bytes_seq}
