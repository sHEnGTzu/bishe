<template>
  <div>
    <!-- 指标显示框 -->
    <div class="indicator-box">
      <p>节点数量: {{ nodeCount }}</p>
      <p>边的数量: {{ edgeCount }}</p>
      <p>平均度: {{ averageDegree }}</p>
      <p>聚类系数: {{ clusteringCoefficient }}</p>
    </div>
    <div id="network"></div>
  </div>
</template>

<script>
import { executeQuery } from '@/neo4j/connection/neo4jService.js';
import { Network } from 'vis-network/standalone/esm/vis-network';

export default {
  name: 'GraphVisualization',
  data() {
    return {
      network: null,
      nodeCount: 0,
      edgeCount: 0,
      averageDegree: 0,
      clusteringCoefficient: 0
    };
  },
  async mounted() {
    // 执行 Cypher 查询获取图谱数据
    const records = await executeQuery('MATCH (n)-[r]->(m) RETURN n, r, m');

    // 处理查询结果，转换为 vis-network 所需的格式
    const nodes = [];
    const edges = [];

    records.forEach((record) => {
      const sourceNode = record.get('n');
      const targetNode = record.get('m');
      const relationship = record.get('r');

      // 添加节点
      if (!nodes.some((node) => node.id === sourceNode.identity.low)) {
        nodes.push({
          id: sourceNode.identity.low,
          label: sourceNode.properties.name || sourceNode.labels[0],
        });
      }
      if (!nodes.some((node) => node.id === targetNode.identity.low)) {
        nodes.push({
          id: targetNode.identity.low,
          label: targetNode.properties.name || targetNode.labels[0],
        });
      }

      // 添加边
      edges.push({
        from: sourceNode.identity.low,
        to: targetNode.identity.low,
        label: relationship.type,
      });
    });

    // 计算指标
    this.nodeCount = nodes.length;
    this.edgeCount = edges.length;
    this.averageDegree = this.edgeCount > 0 ? (2 * this.edgeCount) / this.nodeCount : 0;

    // 简单计算聚类系数（这里只是示例，实际聚类系数计算更复杂）
    let triangleCount = 0;
    for (let i = 0; i < nodes.length; i++) {
      const nodeId1 = nodes[i].id;
      const neighbors1 = edges.filter(edge => edge.from === nodeId1 || edge.to === nodeId1).map(edge => edge.from === nodeId1 ? edge.to : edge.from);
      for (let j = 0; j < neighbors1.length; j++) {
        const nodeId2 = neighbors1[j];
        const neighbors2 = edges.filter(edge => edge.from === nodeId2 || edge.to === nodeId2).map(edge => edge.from === nodeId2 ? edge.to : edge.from);
        for (let k = j + 1; k < neighbors1.length; k++) {
          const nodeId3 = neighbors1[k];
          if (neighbors2.includes(nodeId3)) {
            triangleCount++;
          }
        }
      }
    }
    const possibleTriangles = this.nodeCount * (this.nodeCount - 1) * (this.nodeCount - 2) / 6;
    this.clusteringCoefficient = possibleTriangles > 0 ? triangleCount / possibleTriangles : 0;

    // 创建 vis-network 实例
    const container = document.getElementById('network');
    const data = {
      nodes: nodes,
      edges: edges,
    };
    const options = {};
    this.network = new Network(container, data, options);
  },
  beforeUnmount() {
    if (this.network) {
      this.network.destroy();
    }
  },
};
</script>

<style scoped>
.indicator-box {
  background-color: white;
  padding: 10px;
  border: 1px solid #ccc;
  margin: 10px; /* 添加一些外边距，使显示框与周围元素有一定间隔 */
  display: inline-block; /* 使显示框宽度自适应内容 */
}

#network {
  width: 100%;
  height: 600px;
}
</style>，