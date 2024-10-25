*** Settings ***
Library           SeleniumLibrary
Resource          Variables/browser.resource
Resource          Variables/url.resource
Resource          Keywords/general.resource

*** Test Cases ***
success
    [Tags]    Modify
    ${alert}    login    20021209    1234567
    ${alert}    modify info    nick lyh    1234567    1234567    1234567    山东小学    2761991408@qq.com    20021209
    ${res}    Convert To Boolean    True
    Should Be Equal    ${alert}    ${res}

pwd_err
    [Tags]    Modify
    ${alert}    login    20021209    1234567
    ${alert}    modify info    nick lyh    123456    123456    123456    山东小学    2761991408@qq.com    20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

npwd_too_short
    [Tags]    Modify
    ${alert}    login    20021209    1234567
    ${alert}    modify info    nick lyh    1234567    123    123    山东小学    2761991408@qq.com    20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

rpt_err
    [Tags]    Modify
    ${alert}    login    20021209    1234567
    ${alert}    modify info    nick lyh    1234567    123456    1234567    山东小学    2761991408@qq.com    20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}

school_too_long
    [Tags]    Modify
    ${alert}    login    20021209    1234567
    ${alert}    modify info    nick lyh    1234567    1234567    1234567    山东大学小学中学幼儿园大学四位一体学校    2761991408@qq.com    20021209
    ${res}    Convert To Boolean    False
    Should Be Equal    ${alert}    ${res}
