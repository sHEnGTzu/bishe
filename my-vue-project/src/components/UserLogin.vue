<template>
  <div class="login-container">
    <h2>登录</h2>
    <form>
      <input type="text" v-model="username" placeholder="账号" />
      <input type="password" v-model="password" placeholder="密码" />
      <button type="button" @click="login">登录</button>
      <p></p>
      <button type="button" @click="goToRegister">注册</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import request from '@/utils/request'; // 引入 axios 实例
import router from '@/router/index';

const username = ref('');
const password = ref('');
let message = '';

const login = async () => {
  if (!username.value || !password.value) {
    alert('用户名和密码不能为空');
    return;
  }

  try {
    // 发送登录请求
    const response = await request.post('/login', {
      username: username.value,
      password: password.value
    });

    // 处理登录成功的响应
    message = response.message;
    console.log(message);
    if (message === '登录成功') {
      // 将用户名存储到 localStorage 中
      localStorage.setItem('username', username.value);
      // 跳转到文献列表页面
      router.push({name: 'LiteratureList'});
    } else {
      alert(message);
    }

  } catch (error) {
    // 处理登录失败的情况
    console.error('登录失败:', error);
  }
};

// 跳转到注册页面的方法
const goToRegister = () => {
  router.push({ name: 'Register' });
};
</script>

<style scoped>
.login-container {
  width: 300px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
}

input {
  width: 90%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

button {
  width: 50%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>