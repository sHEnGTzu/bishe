<template>
  <div class="literature-detail-container">
    <h2>{{ literature.title }}</h2>
    <p>文献详细信息：这是一篇关于 {{ literature.title }} 的详细文献内容。</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const literature = ref({ title: '' });

onMounted(() => {
  const id = parseInt(route.params.id);
  // 这里可以根据 id 从后端获取具体文献信息
  literature.value = { id, title: `文献标题 ${id}` };
});
</script>

<style scoped>
.literature-detail-container {
  width: 80%;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>