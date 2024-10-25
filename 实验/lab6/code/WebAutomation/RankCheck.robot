*** Settings ***
Library           SeleniumLibrary
Resource          Keywords/general.resource
Resource          Variables/browser.resource
Resource          Variables/url.resource

*** Test Cases ***
AccurateID
    [Tags]    Rank
    rank check    202100300063

FuzzyID
    [Tags]    Rank
    rank check    2021
