<template>
  <div class="layout-container">
    <el-card class="sidebar-card" shadow="hover">
      <div class="literature-info">
        <h2 class="literature-title">{{ literature.title }}</h2>
        <el-button class="back-button" type="primary" @click="goBack">返回</el-button>
      </div>

      <div class="literature-grid">
        <div class="info-card">
          <el-icon><UserFilled /></el-icon>
          <span><strong>作者：</strong>{{ literature.author || '未提供' }}</span>
        </div>
        <div class="info-card">
          <el-icon><Tickets /></el-icon>
          <span><strong>主题：</strong>{{ literature.subject || '未提供' }}</span>
        </div>
        <div class="info-card">
          <el-icon><CollectionTag /></el-icon>
          <span><strong>关键词：</strong>{{ literature.keywords || '未提供' }}</span>
        </div>
        <div class="info-card">
          <el-icon><Link /></el-icon>
          <span><strong>DOI：</strong>{{ literature.doi || '未提供' }}</span>
        </div>
        <div class="info-card">
          <el-icon><Reading /></el-icon>
          <span><strong>期刊：</strong>{{ literature.journal || '未提供' }}</span>
        </div>
      </div>
    </el-card>

    <!-- 右侧预留区域（可拓展） -->
    <div class="main-content">
      <!-- 可放摘要、图谱、评论区等 -->
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElButton, ElCard } from 'element-plus';
import {
  UserFilled,
  Tickets,
  CollectionTag,
  Link,
  Reading
} from '@element-plus/icons-vue'


const route = useRoute();
const router = useRouter();
const literature = ref({
  title: '',
  author: '',
  subject: '',
  keywords: '',
  doi: '',
  journal: ''
});
const username = ref('');
const paperName = ref('');

onMounted(() => {
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) username.value = storedUsername;
  const storedPapername = localStorage.getItem('paperName');
  if (storedPapername) paperName.value = storedPapername;

  const id = parseInt(route.params.id);
  const author = route.query.author;
  const subject = route.query.subject;
  const keywords = route.query.keywords;
  const DOI = route.query.DOI;
  const Journal = route.query.Journal;

  literature.value = {
    id,
    title: paperName.value,
    author: author,
    subject: subject,
    keywords: keywords,
    doi: DOI,
    journal: Journal
  };
});

const goBack = () => {
  router.push('/literature'); // 返回到首页或其他页面
};
</script>

<style scoped>
.layout-container {
  display: flex;
  width: 100%;
  height: 700px;
  padding: 0px;
  box-sizing: border-box;
}

.sidebar-card {
  width: 320px;
  min-height: 100%;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.main-content {
  flex: 1;
  padding-left: 24px;
  /* 可拓展内容样式 */
}

.literature-info {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
  align-items: center;
  justify-content: space-between;
}

.literature-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.back-button {
  height: 32px;
  line-height: 32px;
  padding: 0 14px;
  font-size: 14px;
}

.literature-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-card {
  align-items: center;
  gap: 8px;
  padding: 8px 10px;
  background-color: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  font-size: 14px;
  color: #333;
  width: 200px; /* 设置固定宽度 */
  overflow-wrap: break-word; /* 同样是强制换行 */
  white-space: normal; /* 使文本正常换行 */
}




.info-card el-icon {
  font-size: 18px;
  color: #409eff;
}


</style>
