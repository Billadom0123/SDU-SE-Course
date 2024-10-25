import re
import sys

import jieba


def remove_punctuation(text):
    # 定义一个正则表达式模式，匹配所有标点符号
    punctuation_pattern = r'[^\w\s]'
    # 使用re.sub替换所有匹配到的标点符号为空字符串
    no_punctuation_text = re.sub(punctuation_pattern, '', text)
    return no_punctuation_text


def list2str(segs: list) -> str:
    ans = ""
    for word in segs:
        if word != "\n":
            word = word + " "
        ans += word
    ans += "\n"
    return ans


if __name__ == '__main__':
    args = sys.argv

    original_path = args[1]
    target_path = args[2]
    dict_path = args[3]

    # 读取文件内容
    with open(original_path, "r", encoding="utf-8") as doc:
        content = doc.read()

    # 对内容进行分词
    jieba.load_userdict(dict_path)
    content = content.replace('\n\n', '\n')
    # content = content.replace('  ', '')
    # content = content.replace('\t', '')
    seg_list = jieba.cut(content, cut_all=False)
    seg_words = list(seg_list)  # 将生成器转换为列表

    res = remove_punctuation(list2str(seg_words))

    # 输出分词结果到目标文件
    with open(target_path, "w", encoding='utf-8') as target:
        target.write(res)
