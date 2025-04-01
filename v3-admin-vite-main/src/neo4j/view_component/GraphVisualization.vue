<template>
  <div id="network"></div>
</template>

<script>
import { executeQuery } from '@/neo4j/connection/neo4jService.js';
import { Network } from 'vis-network/standalone/esm/vis-network';

export default {
  name: 'GraphVisualization',
  data() {
    return {
      network: null,
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

    // 创建 vis-network 实例
    const container = document.getElementById('network');
    const data = {
      nodes: nodes,
      edges: edges,
    };
    const options = {};
    this.network = new Network(container, data, options);
  },
  beforeDestroy() {
    if (this.network) {
      this.network.destroy();
    }
  },
};
</script>

<style scoped>
#network {
  width: 100%;
  height: 600px;
}
</style>
