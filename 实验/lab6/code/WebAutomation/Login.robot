*** Settings ***
Library           SeleniumLibrary
Resource          Variables/url.resource    # 打开的网址
Resource          Variables/browser.resource    # 浏览器
Resource          Keywords/general.resource

*** Test Cases ***
success
    [Tags]    Login
    ${alert}    login    202100300063    P20021209
    ${res}    Convert To Boolean    True
    Should Be Equal    ${alert}    ${res}

pwd_err
    [Tags]    Login
    ${alert}    login    202100300063    pwderr
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

uname_err
    [Tags]    Login
    ${alert}    login    20210030000637    P20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

uname_not_exist
    [Tags]    Login
    ${alert}    login    unamenotexist    P20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

uname_format_err
    [Tags]    Login
    ${alert}    login    안녕하세요    P20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

*** Keywords ***
