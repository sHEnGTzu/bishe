<template>
  <div class="literature-list-container">
    <h2>文献列表</h2>

    <p>欢迎，{{ username }}</p>

    <!-- 上传按钮 -->
    <el-button
        type="primary"
        @click="handleClickUpload"
        class="upload-button">
      上传文献
    </el-button>

    <!-- 上传文件组件 -->
    <input type="file" ref="fileInput" accept="application/pdf" style="display: none" @change="handleFileChange" />
    <p></p>

    <!-- 文献列表 -->
    <el-card
        v-for="literature in literatures"
        :key="literature.id"
        class="literature-card"
        @click="goToDetail(literature.id, literature.title, literature.author, literature.subject, literature.keywords, literature.DOI, literature.Journal)">
      <h3>{{ literature.title }}</h3>
      <el-divider></el-divider>
      <p>作者：{{ literature.author }}</p>
      <p>主题：{{ literature.subject }}</p>
      <p>关键词：{{ literature.keywords }}</p>
      <p>DOI：{{ literature.DOI }}</p>
      <p>期刊：{{ literature.Journal }}</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';

const router = useRouter();
const literatures = ref([]);
const username = ref('');
const uploadUrl = '/uploadFile';  // 上传的接口地址

// 获取当前用户文献列表
const getPapers = async () => {
  try {
    const response = await request.post('/getPapers', {
      username: username.value,
    });
    console.log(response);

    literatures.value = response.paperList.map(paper => ({
      id: paper.id,
      title: paper.paper_name,
      author: paper.author,
      subject: paper.subject,
      keywords: paper.keywords,
      DOI: paper.doi,
      Journal: paper.journal
    }));
    console.log(literatures.value);
  } catch (error) {
    console.error('获取文献列表失败:', error);
  }
};

// 文件选择事件
const handleClickUpload = () => {
  const fileInput = document.querySelector('input[type="file"]');
  fileInput.click();  // 点击文件选择框
};

// 处理文件变化
const handleFileChange = async (event) => {
  const file = event.target.files[0];

  // 上传文件前的验证
  if (file && file.type !== 'application/pdf') {
    alert('请上传 PDF 文件');
    return;
  }

  if (file) {
    await uploadFile(file);  // 触发文件上传
  }
};

// 上传文件
const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);  // 添加文件
  formData.append('username', username.value);  // 添加用户名

  try {
    // 使用 request 发送表单数据
    const response = await request.post(uploadUrl, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'  // 指定请求头为multipart/form-data
      },
      timeout: 0
    });
    console.log('文件上传成功', response);
    alert('文件上传成功');
    await getPapers();  // 上传后重新获取文献列表
  } catch (error) {
    console.error('文件上传失败', error);
    alert('文件上传失败');
  }
};

onMounted(async () => {
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    username.value = storedUsername;
  }
  await getPapers();  // 获取文献列表
});

const goToDetail = (id, paperName, author, subject, keywords, DOI, Journal) => {
  localStorage.setItem('paperName', paperName);
  router.push({ name: 'LiteratureDetail', params: { id }, query: { author,subject,keywords,DOI,Journal } });
};
</script>

<style scoped>
.literature-list-container {
  width: 80%;
  margin: 50px auto;
}

.el-card {
  margin-bottom: 20px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;  /* 添加平滑过渡 */
}

/* 鼠标悬停时的背景色变化 */
.el-card:hover {
  background-color: #f9f9f9;  /* 背景色变化 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  /* 添加阴影 */
}

/* 鼠标悬停时指针变为手形 */
.el-card:hover {
  cursor: pointer;
}

/* 分隔线的样式 */
.el-divider {
  margin: 10px 0;
}

/* 上传按钮的样式 */
.upload-button {
  /* 确保文字垂直居中 */
  padding: 10px 20px;  /* 调整内边距，确保内容适中 */
  font-size: 16px;  /* 字体大小 */
  line-height: 1.5;  /* 调整行高确保垂直居中 */
  display: inline-flex;
  align-items: center;  /* 使图标和文字居中对齐 */
  justify-content: center;  /* 确保水平居中 */
  transition: background-color 0.3s ease, transform 0.2s ease;  /* 添加悬停效果 */
}

/* 鼠标悬停时的效果 */
.upload-button:hover {
  background-color: #409eff;  /* 鼠标悬停时的背景色 */
  transform: scale(1.05);  /* 鼠标悬停时的放大效果 */
}

/* 鼠标点击时的效果 */
.upload-button:active {
  background-color: #66b1ff;  /* 点击时的背景色 */
  transform: scale(0.98);  /* 点击时的缩小效果 */
}
</style>
