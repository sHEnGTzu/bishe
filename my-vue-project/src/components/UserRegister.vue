<template>
  <div class="register-container">
    <h2>注册</h2>
    <el-form :model="form" ref="formRef" label-width="50px">
      <el-form-item label="账号" :rules="[{ required: true, message: '请输入账号', trigger: 'blur' }]">
        <el-input v-model="form.username" placeholder="请输入账号" />
      </el-form-item>

      <el-form-item label="密码" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
        <el-input v-model="form.password" type="password" placeholder="请输入密码" />
      </el-form-item>

      <el-form-item label="邮箱" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="register">注册</el-button>
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
  password: '',
  email: ''
});

const register = async () => {
  // 这里做一些表单验证
  if (!form.value.username || !form.value.password || !form.value.email) {
    alert('用户名、密码和邮箱不能为空');
    return;
  }

  try {
    // 发送注册请求
    const response = await request.post('/register', {
      username: form.value.username,
      password: form.value.password,
      email: form.value.email
    });

    // 处理注册成功的响应
    alert(response.message);
    await router.push({ name: 'Login' });

  } catch (error) {
    // 处理注册失败的情况
    console.error('注册失败:', error);
  }
};
</script>

<style scoped>
.register-container {
  width: 400px;
  margin: 100px auto;
  padding: 40px;
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
  width: 100%;
  height: 40px;
}

.el-input {
  width: 100%;
}

@media (max-width: 600px) {
  .register-container {
    width: 90%;
  }
}
</style>
