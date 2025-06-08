<template>
  <div class="login-container">
    <h2>登录</h2>
    <el-form :model="form" ref="formRef" label-width="50px">
      <el-form-item label="账号" :rules="[{ required: true, message: '请输入账号', trigger: 'blur' }]">
        <el-input v-model="form.username" placeholder="请输入账号" />
      </el-form-item>

      <el-form-item label="密码" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="login" style="width: 100%">登录</el-button>
      </el-form-item>

      <el-form-item>
        <el-button type="text" @click="goToRegister" style="width: 100%">没有账号？去注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import request from '@/utils/request'; // 引入 axios 实例
import router from '@/router/index';

const form = ref({
  username: '',
  password: ''
});

const login = async () => {
  // 校验表单
  if (!form.value.username || !form.value.password) {
    alert('用户名和密码不能为空');
    return;
  }

  try {
    // 发送登录请求
    const response = await request.post('/login', {
      username: form.value.username,
      password: form.value.password
    });

    // 处理登录成功的响应
    const message = response.message;
    console.log(message);
    if (message === '登录成功') {
      // 将用户名存储到 localStorage 中
      localStorage.setItem('username', form.value.username);
      // 跳转到文献列表页面
      router.push({ name: 'LiteratureList' });
    } else {
      alert(message);
    }

  } catch (error) {
    // 处理登录失败的情况
    console.error('登录失败:', error);
  }
};

const goToRegister = () => {
  router.push({ name: 'Register' });
};
</script>

<style scoped>
.login-container {
  width: 350px;
  margin: 100px auto;
  padding: 30px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f9f9f9;
}

h2 {
  font-size: 24px;
  margin-bottom: 20px;
  text-align: center;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button {
  height: 40px;
  font-size: 16px;
}

.el-input {
  width: 100%;
}

@media (max-width: 600px) {
  .login-container {
    width: 90%;
  }
}
</style>
