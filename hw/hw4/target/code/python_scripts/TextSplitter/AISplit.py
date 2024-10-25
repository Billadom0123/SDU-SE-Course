import os
import re
import sys

from zhipuai import ZhipuAI


def remove_punctuation(text):
    # 定义一个正则表达式模式，匹配所有标点符号
    punctuation_pattern = r'[^\w\s]'
    # 使用re.sub替换所有匹配到的标点符号为空字符串
    no_punctuation_text = re.sub(punctuation_pattern, '', text)
    return no_punctuation_text


def request_and_write_res(original_file: str, target_file: str):
    print("子任务"+original_file+"进行中")
    with open(original_file, "r", encoding="utf-8") as doc:
        content = doc.read()

    try:
        messages = [{"role": "user",
                     "content": f"你是一名古汉语的专家。下面给你一段来自三国演义的文本，请你将其分词，例如\"家贫好学\"，分词为\"家贫\"和\"好学\"，并去掉标点符号，每个词之间要用一个空格进行分隔：\n{content}"}]
        response = client.chat.completions.create(
            model="glm-4",
            messages=messages,
        )
    except Exception as e:
        print(e)
    else:
        with open(target_file, "a", encoding='utf-8') as target:
            target.write(response.choices[0].message.content)


if __name__ == '__main__':
    args = sys.argv
    api_key = args[1]
    original_dir = args[2]
    target_path = args[3]

    client = ZhipuAI(api_key=api_key)

    for file_path in os.listdir(original_dir):
        text_path = os.path.join(original_dir, file_path)
        if os.path.isfile(text_path):
            request_and_write_res(text_path, target_path)
