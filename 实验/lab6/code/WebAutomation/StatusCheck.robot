*** Settings ***
Library           SeleniumLibrary
Resource          Keywords/general.resource
Resource          Variables/browser.resource
Resource          Variables/url.resource

*** Test Cases ***
ByID
    [Tags]    Status
    status check    1000    \    0    0

ByUID
    [Tags]    Status
    status check    \    202100300063    0    0

ByLang
    [Tags]    Status
    # 0 all 1 C 2 C++ 3 Java 4 Python
    status check    \    \    3    0

ByRes
    [Tags]    Status
    # AC PE WA TLE MLE OL RE CE WAIT REJ COMPLING RUNNING AND JUDGING
    status check    \    \    0    1

ByAll
    [Tags]    Status
    status check    1008    202100300063    3    1
