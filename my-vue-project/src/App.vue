<template>
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue';
import router from './router/index.js';

onMounted(() => {
  // 初始跳转到登录页面
  router.push('/login');

});
</script>

<style>
body {
  font-family: Arial, sans-serif;
}
</style>