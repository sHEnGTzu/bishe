<template>
  <div class="literature-list-container">
    <h2>文献列表</h2>
    <p>欢迎，{{ username }}</p>
    <ul>
      <li v-for="literature in literatures" :key="literature.id" @click="goToDetail(literature.id)">
        {{ literature.title }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const literatures = ref([
  { id: 1, title: '文献标题 1' },
  { id: 2, title: '文献标题 2' },
  { id: 3, title: '文献标题 3' }
]);
const username = ref('');

onMounted(() => {
  // 从 localStorage 中获取用户名
  const storedUsername = localStorage.getItem('username');
  if (storedUsername) {
    username.value = storedUsername;
  }
});

const goToDetail = (id) => {
  router.push({ name: 'LiteratureDetail', params: { id } });
};
</script>

<style scoped>
.literature-list-container {
  width: 80%;
  margin: 50px auto;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}

li:hover {
  background-color: #f0f0f0;
}
</style>