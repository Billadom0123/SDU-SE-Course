import sys

import happybase
from Lab1.dataDump.dataDump import generate_row_key as grk

if __name__ == '__main__':
    args = sys.argv

    '''
        args :
            1 数据库连接地址
            2 sliding window data 表名
            3 duplicate data 表名
    '''
    url = args[1]
    sw_table = args[2]
    duplicate_table = args[3]

    connection = happybase.Connection(url)


    def pickTable(table_name):
        if table_name not in connection.tables():
            print("Table " + table_name + " not found!Exit...")
            exit(130)
        table = connection.table(table_name)
        return table


    def pickRow(table: happybase.Table, row_key, col):
        try:
            data = table.row(row_key, columns=[col])
            return data
        except Exception as e:
            print("Error fetching data: ", e)


    duplicate_data = pickTable(duplicate_table)
    meta_data = pickTable(sw_table)
    w_size = int(pickRow(meta_data, "window_size", "window_size"))

    print("You have 3 input options to choose:")
    print("1:search the sliding window and its label")
    print("2:search the original duplicate dataset")
    print("3:search all y labels in a sliding window")
    print("4/others:exit")

    while True:
        flag = input("Give me your choice")
        if flag == '1' or flag == '2' or flag == '3':
            i = int(input("input i:"))
            j = int(input("input j:"))
            k = int(input("input k:"))
            col_qualifier = "y_label:value"
            if flag == '1':
                rk = grk(i, j, k)
                print(rk+":"+col_qualifier, pickRow(meta_data, rk, col_qualifier))
            elif flag == '2':
                rk = grk(i, j, k)
                print(rk + ":" + col_qualifier, pickRow(duplicate_data, rk, col_qualifier))
            else:
                for x in range(i + w_size):
                    rk = grk(x, j, k)
                    print(rk+":"+col_qualifier, pickRow(duplicate_data, rk, col_qualifier))
        else:
            exit(0)