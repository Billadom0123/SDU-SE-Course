*** Settings ***
Library           SeleniumLibrary

*** Keywords ***
login
    [Arguments]    ${username}    ${password}
    # 打开浏览器并加载网页
    Open Browser    ${login}    ${ff}
    # 输入用户名密码
    Input Text    id = username    ${username}
    Input Password    id = password    ${password}
    # 点击登录按钮
    Click Button    name = submit
    # 检查登录是否成功
    ${alert}    Run Keyword And Return Status    Execute Javascript    return !!window.alert
    Run Keyword If    ${alert}    Log    用户${username}登录成功
    ...    ELSE    Log    用户${username}登陆失败
    Return From Keyword    ${alert}

submit code
    [Arguments]    ${id}    ${code}    ${lang}
    # 拼接url
    ${submit_url}    Set Variable    ${submit}?id=${id}
    # 打开对应页面
    Go To    ${submit_url}
    # 填充语言选择
    Select From List By Index    id = language    ${lang}    # 这个lang是索引，而非值
    # 填充代码
    ${text_area}    Get WebElement    tag = textarea    # 获取textarea元素
    ${js_code}=    Catenate    SEPARATOR=;    arguments[0].style.opacity = 1;arguments[0].style.left = 0;
    Execute JavaScript    ${js_code}    ARGUMENTS    ${text_area}
    Scroll Element Into View    ${text_area}
    Input Text    tag = textarea    ${code}    # 填充代码
    # 点击提交按钮
    Click Button    id = Submit

check submit result
    [Arguments]    ${username}    ${should_be_result}
    # 检查结果
    ${uStatus}    Set Variable    ${status}?user_id=${username}
    Go To    ${uStatus}
    Sleep    10    # 10s内测评必定结束
    @{tds}    Get WebElements    class = label
    ${res}    Set Variable    ${tds}[0]
    ${result}    Get Text    ${res}
    Log    代码评测结果为${result}
    Should Start With    ${result}    ${should_be_result}

register
    [Arguments]    ${uname}    ${nickname}    ${pwd}    ${repeat}    ${school}    ${email}
    Open Browser    ${register}    ${ff}
    Input Text    name = user_id    ${uname}
    Input Text    name = nick    ${nickname}
    Input Text    name = password    ${pwd}
    Input TEXT    name = rptpassword    ${repeat}
    Input Text    name = school    ${school}
    Input Text    name = email    ${email}
    Click Button    name = submit
    ${alert}    Run Keyword And Return Status    Execute Javascript    return !!window.alert
    Run Keyword If    ${alert}    Log    用户${uname}登录成功
    ...    ELSE    Log    用户${uname}登陆失败
    Return From Keyword    ${alert}

modify info
    [Arguments]    ${nickname}    ${opwd}    ${npwd}    ${rpt}    ${school}    ${email}    ${uname}
    Go To    ${modify_info}
    Input Text    name = nick    ${nickname}
    Input Text    name = opassword    ${opwd}
    Input Text    name = npassword    ${npwd}
    Input Text    name = rptpassword    ${rpt}
    Input Text    name = school    ${school}
    Input Text    name = email    ${email}
    Click Button    name = submit
    ${alert}    Run Keyword And Return Status    Execute Javascript    return !!window.alert
    Run Keyword If    ${alert}    Log    用户${uname}修改成功
    ...    ELSE    Log    用户${uname}修改失败
    Return From Keyword    ${alert}

problem search
    [Arguments]    ${title}    ${id}
    Open Browser    ${problemset}    ${ff}
    Input Text    name = search    ${title}
    Input Text    name = id    ${id}
    IF    '${id}' != '${EMPTY}'
        Press Key    name:id    \\\13
    ELSE
        Press Key    name:search    \\\13
    END

status check
    [Arguments]    ${id}    ${uid}    ${lang}    ${res}
    Open Browser    ${status}    ${ff}
    Input Text    name = problem_id    ${id}
    Input Text    name = user_id    ${uid}
    Select From List By Index    name = language    ${lang}
    Select From List By Index    name = jresult    ${res}
    Click Button    tag = button

rank check
    [Arguments]    ${uid}
    Open Browser    ${rank}    ${ff}
    Input Text    name = prefix    ${uid}
    Click Button    tag = button
