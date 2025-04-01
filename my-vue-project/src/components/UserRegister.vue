<template>
  <div class="register-container">
    <h2>注册</h2>
    <form>
      <input type="text" v-model="username" placeholder="账号" />
      <input type="password" v-model="password" placeholder="密码" />
      <button type="button" @click="register">注册</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import request from '@/utils/request'; // 引入 axios 实例
import router from '@/router/index';

const username = ref('');
const password = ref('');
let message = ref('');

const register = async () => {
  if (!username.value || !password.value) {
    alert('用户名和密码不能为空');
    return;
  }
  try {
    // 发送登录请求
    const response = await request.post('/register', {
      username: username.value,
      password: password.value
    });

    // 处理登录成功的响应
    message = response.message;
    alert(message);
    await router.push({name: 'Login'});

  } catch (error) {
    // 处理登录失败的情况
    console.error('注册失败:', error);
  }
};
</script>

<style scoped>
.register-container {
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
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}
</style>