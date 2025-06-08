from operator import truediv
import requests
import os
from bs4 import BeautifulSoup
import sys

# 设置请求头，模拟浏览器访问
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'}

def init(url,num):
    count = 0
    # 发送 HTTP 请求
    response = requests.get(url, headers=headers,stream=True)

    if response.status_code == 200:
        # 解析 HTML 内容
        soup = BeautifulSoup(response.text, 'html.parser')
        # 查找文献标题和链接
        articles_temp = soup.find_all('article')
        articles = []
        for article in articles_temp:
            if kmp_search(str(article),"Open Access"):
                articles.append(article)
                count = count + 1
            if count == num: break
        


        for article in articles:
            title = article.find('a').text.strip()
            link = 'https://www.nature.com' + article.find('a')['href']
            print(f'Title: {title}')
            print(f'Link: {link}')
            download_pdf(f'{link}',"G:\\code\\bishe\\bishe\\src\\main\\java\\com\\example\\bishe\\pdf_paper",f'{title}')
    else:
        print(f'Failed to access the website: {url}')


#下载文献
def download_pdf(url, save_path,name):
    try:
        # 发送请求获取文件内容
        response = requests.get(url+".pdf",headers=headers,stream=True)
        response.raise_for_status()  # 检查请求是否成功

        # 拼接完整的保存路径
        full_save_path = os.path.join(save_path, name+".pdf")

        # 以二进制写入模式打开文件
        with open(full_save_path, 'wb') as file:
            # 分块写入文件，避免一次性加载整个文件到内存
            for chunk in response.iter_content(chunk_size=8192):
                if chunk:
                    file.write(chunk)

        print(f"文件已成功下载到 {full_save_path}")
    except requests.exceptions.RequestException as e:
        print(f"请求出错: {e}")
    except Exception as e:
        print(f"下载过程中出现其他错误: {e}")



#以下两个函数为KMP算法
def compute_lps(target):
    # 计算最长前缀后缀数组
    m = len(target)
    lps = [0] * m
    length = 0
    i = 1
    while i < m:
        if target[i] == target[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]
            else:
                lps[i] = 0
                i += 1
    return lps

def kmp_search(content, target):
    # KMP 搜索算法
    n = len(content)
    m = len(target)
    lps = compute_lps(target)
    i = 0  # 指向 content 的索引
    j = 0  # 指向 target 的索引
    while i < n:
        if target[j] == content[i]:
            i += 1
            j += 1
        if j == m:
            return True
        elif i < n and target[j] != content[i]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1
    return False

# init('https://www.nature.com/nature/articles?type=article',1)
if __name__ == "__main__":
    if len(sys.argv) == 3:
        url = str(sys.argv[1])
        num = int(sys.argv[2])
        init(url,num)
    else:
        print("Please offer correct argv.")