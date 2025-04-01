<template>
  <div class="chat-box">
    <!-- 聊天消息显示区域 -->
    <div class="chat-messages">
      <div v-for="(message, index) in messages" :key="index" :class="message.sender === 'user' ? 'user-message' : 'bot-message'">
        <div class="message-box">{{ message.text }}</div>
      </div>
    </div>
    <!-- 输入框和发送按钮 -->
    <div class="chat-input">
      <input v-model="inputMessage" placeholder="输入你的消息" @keyup.enter="sendMessage">
      <button @click="sendMessage">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

// 存储聊天消息的数组
const messages = ref([]);
// 存储用户输入的消息
const inputMessage = ref('');

// 发送消息的函数
const sendMessage = async () => {
  if (inputMessage.value.trim() === '') return;

  // 将用户输入的消息添加到消息列表中
  const userMessage = { sender: 'user', text: inputMessage.value };
  messages.value.push(userMessage);

  // 清空输入框
  inputMessage.value = '';

  try {
    // 发送消息到后端
    const response = await axios.post('http://localhost:8081/question', {
      message: userMessage.text
    });

    // 将后端返回的消息添加到消息列表中
    const botMessage = { sender: 'bot', text: response.data.message };
    messages.value.push(botMessage);
  } catch (error) {
    console.error('发送消息失败:', error);
    // 可以在这里添加错误处理逻辑，例如显示错误消息给用户
  }
};
</script>

<style scoped>
.chat-box {
  width: 700px;
  height: 700px;
  border: 1px solid #ccc;
  border-radius: 5px;
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
}

.user-message {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.bot-message {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 10px;
}

.message-box {
  max-width: 70%;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  white-space: pre-wrap; /* 保留空白和换行符 */
}

.user-message .message-box {
  background-color: #dcf8c6;
  color: #333;
}

.bot-message .message-box {
  background-color: #fff;
  color: #333;
  border: 1px solid #e0e0e0;
}

.chat-input {
  display: flex;
  padding: 10px;
  border-top: 1px solid #ccc;
}

.chat-input input {
  flex: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

.chat-input button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.chat-input button:hover {
  background-color: #0056b3;
}
</style>