<template>

  <el-container>
    <el-row>
      <el-col :span="5">
        <LiteratureDetail/>
      </el-col>
      <el-col :span="9">
        <ChatBox/>
      </el-col>
      <el-col :span="10">
        <GraphVisualization/>
      </el-col>
    </el-row>
  </el-container>

</template>

<script setup>
import LiteratureDetail from '../components/LiteratureDetail.vue';
import GraphVisualization from '../neo4j/view_component/GraphVisualization.vue';
import ChatBox from './ChatBox.vue';

</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px; /* 可根据需要调整 */
}

.left {
  width: 48%; /* 可根据需要调整 */
}

.right {
  width: 48%; /* 可根据需要调整 */
}
</style>