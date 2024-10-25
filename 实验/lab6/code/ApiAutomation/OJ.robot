*** Settings ***
Library           SeleniumLibrary
Resource          Keywords/general.resource
Resource          Variables/api.resource
Resource          ../WebAutomation/Variables/url.resource
Resource          ../WebAutomation/Variables/browser.resource
Resource          ../WebAutomation/Variables/code.resource

*** Variables ***
${session}        PHPSESSID    # cookie
${uid}            202100300063    # 能正确登录的账号
${pwd}            P20021209    # 能正确登录的密码

*** Test Cases ***
1000_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1000
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1000_AC}    &{cookies}
    check submit result    ${uid}    正确

1001_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1001
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1001_AC}    &{cookies}
    check submit result    ${uid}    正确

1002_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1002
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1002_AC}    &{cookies}
    check submit result    ${uid}    正确

1003_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1003
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1003_AC}    &{cookies}
    check submit result    ${uid}    正确

1004_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1004
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1004_AC}    &{cookies}
    check submit result    ${uid}    正确

1005_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1005
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1005_AC}    &{cookies}
    check submit result    ${uid}    正确

1006_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1006
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1006_AC}    &{cookies}
    check submit result    ${uid}    正确

1007_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1007
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1007_AC}    &{cookies}
    check submit result    ${uid}    正确

1008_AC
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1008
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1008_AC}    &{cookies}
    check submit result    ${uid}    正确

1000_MLE
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1000
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1000_MLE}    &{cookies}
    check submit result    ${uid}    内存超限

1003_TLE
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1003
    ${lang}    Convert To Integer    6    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1003_TLE}    &{cookies}
    check submit result    ${uid}    时间超限

1005_WA
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1005
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1005_WA}    &{cookies}
    check submit result    ${uid}    答案错误

1000_CE
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1000
    ${lang}    Convert To Integer    0    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1000_CE}    &{cookies}
    check submit result    ${uid}    编译错误

1004_RE
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1004
    ${lang}    Convert To Integer    6    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1004_RE}    &{cookies}
    check submit result    ${uid}    运行错误

1006_PE
    [Tags]    PostOJ
    &{cookies}    login    ${uid}    ${pwd}
    Log    ${cookies}    # 打印cookie
    ${id}    Convert To Integer    1006
    ${lang}    Convert To Integer    3    # 0 1 3 6 C C++ Java Python
    submit code    ${id}    ${lang}    ${1006_PE}    &{cookies}
    check submit result    ${uid}    格式错误
