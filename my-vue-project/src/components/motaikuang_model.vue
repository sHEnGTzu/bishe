<template>
  <div>
    <!-- 按钮，点击触发 openDialog 方法 -->
    <el-button @click="openDialog">打开模态框</el-button>
    <!-- ElDialog 组件 -->
    <el-dialog v-model=dialogVisible
        title="这是模态框标题"
        width="30%">
      <!-- 模态框内容 -->
        <p>这里是模态框内的指定内容。</p>
      <!-- 模态框底部操作按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <!-- 取消按钮，点击关闭模态框 -->
          <el-button @click="dialogVisible = false">取消</el-button>
          <!-- 确定按钮，点击关闭模态框 -->
          <el-button type="primary" @click="dialogVisible = false">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// 定义一个响应式变量来控制模态框的显示与隐藏
const dialogVisible = ref(false);

// 打开模态框的方法
const openDialog = () => {
  dialogVisible.value = true;
};
</script>

<style scoped>
/* 可以根据需要添加自定义样式 */
</style>