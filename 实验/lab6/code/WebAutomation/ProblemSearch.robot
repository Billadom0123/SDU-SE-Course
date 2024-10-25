*** Settings ***
Library           SeleniumLibrary
Resource          Keywords/general.resource
Resource          Variables/url.resource
Resource          Variables/browser.resource

*** Test Cases ***
ByExistID
    [Tags]    Problem
    problem search    \    1000

ByNotExistID
    [Tags]    Problem
    problem search    \    999

FuzzyByTitle
    [Tags]    Problem
    problem search    算法    \

PreciseByTitle
    [Tags]    Problem
    problem search    迷宫问题    \

None
    [Tags]    Problem
    problem search    \    \
