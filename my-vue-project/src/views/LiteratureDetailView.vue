<template>
  <LiteratureDetail/>
  <div className="container">
    <div className="left">
      <ChatBox/>
    </div>
    <div className="right">
      <GraphVisualization/>
    </div>
  </div>
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