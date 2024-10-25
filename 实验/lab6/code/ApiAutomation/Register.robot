*** Settings ***
Resource          ../WebAutomation/Variables/browser.resource
Resource          ../WebAutomation/Variables/url.resource
Resource          Keywords/general.resource
Resource          Variables/api.resource

*** Test Cases ***
success
    [Tags]    PostRegister
    register    200203214    hahahah    123456    123456    sdu    3054117680@qq.com    -2

uname_format_err
    [Tags]    PostRegister
    register    안녕하세요    hahahah    123456    123456    sdu    3054117680@qq.com    alert

uname_too_short
    [Tags]    PostRegister
    register    1    hahahah    123456    123456    sdu    3054117680@qq.com    alert

pwd_too_short
    [Tags]    PostRegister
    register    20020429    hahahah    123    123    sdu    3054117680@qq.com    alert

pwd_rpt_err
    [Tags]    PostRegister
    register    20020529    hahahah    123456    1234567    sdu    3054117680@qq.com    alert

school_too_long
    [Tags]    PostRegister
    register    20020629    hahahah    123456    123456    我的学校的名字好长好长好长好长好长好长好长好长好长好长    3054117680@qq.com    alert
