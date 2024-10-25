*** Settings ***
Library           SeleniumLibrary
Resource          Variables/url.resource
Resource          Variables/browser.resource
Resource          Keywords/general.resource

*** Test Cases ***
success
    [Tags]    Register
    ${alert}    register    20021209    nickname    123456    123456    sdu    3054117680@qq.com
    ${res}    Convert To Boolean    True
    Should Be Equal    ${alert}    ${res}

uname_format_err
    [Tags]    Register
    ${alert}    register    안녕하세요    nickname    123456    123456    sdu    3054117680@qq.com
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

uname_too_short
    [Tags]    Register
    ${alert}    register    1    nickname    123456    123456    sdu    3054117680@qq.com
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

pwd_too_short
    [Tags]    Register
    ${alert}    register    20021209    nickname    123    123    sdu    3054117680@qq.com
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

pwd_rpt_err
    [Tags]    Register
    ${alert}    register    20021209    nickname    123456    1234567    sdu    3054117680@qq.com
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

school_too_long
    [Tags]    Register
    ${alert}    register    20021209    nickname    123456    123456    我的学校的名字好长好长好长好长好长好长好长好长好长好长    3054117680@qq.com
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}
