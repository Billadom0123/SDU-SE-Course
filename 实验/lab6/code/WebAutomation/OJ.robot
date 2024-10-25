*** Settings ***
Library           SeleniumLibrary
Resource          Variables/url.resource    # 网页链接
Resource          Variables/browser.resource    # 浏览器驱动
Resource          Variables/code.resource    # 代码
Resource          Keywords/general.resource

*** Variables ***
${username}       202100300063
${password}       P20021209
${header}         PHPSESSID

*** Test Cases ***
AC
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1008    ${1008_AC}    2    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    正确

TLE
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1003    ${1003_TLE}    3    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    时间超限

MLE
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1000    ${1000_MLE}    2    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    内存超限

WA
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1005    ${1005_WA}    2    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    答案错误

CE
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1000    ${1000_CE}    0    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    编译错误

RE
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1004    ${1004_RE}    3    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    运行错误

PE
    [Tags]    OJ
    ${token}    login    ${username}    ${password}
    submit code    1006    ${1006_PE}    2    # 0 1 2 3 C C++ Java Python
    check submit result    ${username}    格式错误

dummy
    Log    ${1008_AC}
    Log    ${1000_OL}
    Log    ${1000_OL_prefix}
    Log    ${1000_OL_suffix}
    Log    ${1000_OL_prefix}\n${1000_OL_suffix}
    ${tmp}    Set Variable    #include<iostream>
    Log    ${tmp}
