*** Test Cases ***
logSomething
    Log    BuiltIn Log Keyword Log    # 打印
    # 定义变量
    ${a}    Set Variable    20
    Log    ${a}
    # 获得系统时间
    ${time}    Get Time
    # 验证两个值是否相等
    ${assert_res}    Run Keyword And Return Status    Should Be Equal    ${a}    20
    Log    ${assert_res}
    # 验证一个字符串是否包含另一个字符串
    Should Contain    I love software test!    love
    # os库 运行关键字并忽略错误
    Run Keyword And Ignore Error    Log    Man!
    # 将数据转换为二进制格式
    ${bs}    Convert To Binary    ${20}
    Log    ${bs}
