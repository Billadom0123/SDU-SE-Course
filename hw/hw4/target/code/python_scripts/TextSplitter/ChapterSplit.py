import os
import re
import sys


def split_chapters_and_save(source_path, target_dir):
    # 确保目标文件夹存在
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)

    with open(source_path, 'r', encoding='utf-8') as file:
        text = file.read()

        # 正则表达式匹配章回格式，如“第四十七回”或“第47回”
    chapter_pattern = re.compile(r'第([一二三四五六七八九十百千万亿\d]+)回')

    # 找到所有章回标注的索引以及对应的章回序号
    chapter_indices = [(m.start(), m.group(1)) for m in chapter_pattern.finditer(text)]

    # 如果没有找到任何章回标注，直接返回
    if not chapter_indices:
        print("没有找到任何章回标注。")
        return

        # 分割文本为章回列表，同时保存每个章回的序号
    chapters = []
    for i in range(len(chapter_indices)):
        start_index = chapter_indices[i][0]
        if i<len(chapter_indices)-1:
            end_index = chapter_indices[i+1][0]
        else:
            end_index = len(text)-1
        chapters.append((i, text[start_index:end_index]))

        # 保存每个章回到单独的文件
    for i, (chapter_num, chapter) in enumerate(chapters, start=1):
        chapter_file_path = os.path.join(target_dir, f"第{chapter_num}回.txt")
        with open(chapter_file_path, 'w', encoding='utf-8') as chapter_file:
            chapter_file.write(chapter)

    print(f"所有章回已保存到 {target_dir}")


if __name__ == '__main__':
    args = sys.argv

    source_path = args[1]
    target_dir = args[2]

    split_chapters_and_save(source_path, target_dir)
