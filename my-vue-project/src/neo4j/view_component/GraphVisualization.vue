<template>
  <div class="graph-container">
    <!-- 搜索框 -->
    <div class="search-box">
      <el-input v-model="query_name" placeholder="搜索节点名称" @keyup.enter="query_by_name_type"></el-input>
      <el-button @click="query_by_name_type">搜索</el-button>
    </div>

    <div class="xiala">
      <!-- 下拉选择框 -->
      <el-select v-model="selectedType" placeholder="节点类型" @onchange="query_by_name_type">
        <el-option
            v-for="option in nodeTypes"
            :key="option.value"
            :label="option.label"
            :value="option.value">
        </el-option>
      </el-select>
    </div>

    <!-- 指标显示框 -->
    <div class="indicator-box">
      统计
      <p>节点数量: {{ nodeCount }}</p>
      <p>边的数量: {{ edgeCount }}</p>
      <p>平均度: {{ averageDegree }}</p>
    </div>
    <!-- 按钮组 -->
    <div class="button-group">
      <el-button @click="openEdgeDialog">边类型统计</el-button>
      <el-button @click="openNodeDialog">结点类型统计</el-button>
    </div>

    <!-- 图容器 -->
    <div id="network"></div>
    <!-- 连通性查询 -->
    <div class="connectivity-check">
      <el-input v-model="connectNodeA" placeholder="节点A名称" style="width: 200px;"></el-input>
      <el-input v-model="connectNodeB" placeholder="节点B名称" style="width: 200px;"></el-input>
      <el-button @click="checkConnectivity">检查连通性</el-button>
    </div>

    <div>
      <!-- ElDialog 组件 -->
      <el-dialog v-model="edgeDialogVisible" title="边类型统计" width="30%">
        <!-- 模态框内容 -->
        <!-- 边名柱状图 -->
        <canvas id="edgeChart"></canvas>
        <!-- 模态框底部操作按钮 -->
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleConfirm_edge">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
    <div>
      <!-- ElDialog 组件 -->
      <el-dialog v-model="nodeDialogVisible" title="结点类型统计" width="30%">
        <!-- 模态框内容 -->
        <!-- 节点名柱状图 -->
        <canvas id="nodeChart"></canvas>
        <!-- 模态框底部操作按钮 -->
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleConfirm_node">关闭</el-button>
          </span>
        </template>
      </el-dialog>

    </div>

    <!-- 连通性检查结果弹窗 -->
    <el-dialog v-model="connectivityDialogVisible" title="连通性检查结果" width="30%">
      <p>{{ connectivityResult }}</p>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="connectivityDialogVisible = false">关闭</el-button>
    </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { executeQuery } from '@/neo4j/connection/neo4jService.js';
import { Network } from 'vis-network/standalone/esm/vis-network';
import Chart from 'chart.js/auto';
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';

export default {
  name: 'GraphVisualization',
  setup() {
    const network = ref(null);
    const nodeCount = ref(0);
    const edgeCount = ref(0);
    const averageDegree = ref(0);
    const edgeNameCounts = ref({});
    const nodeTypeCounts = ref({});
    const edgeDialogVisible = ref(false);
    const nodeDialogVisible = ref(false);
    let edgeChartInstance = null;
    let nodeChartInstance = null;
    const paperName = ref({});
    const query_name = ref('');
    //下拉框
    const selectedType = ref(''),
        nodeTypes = [
          { label: 'all', value: 'all' },
      { label: 'Task', value: 'Task' },
      { label: 'Method', value: 'Method' },
      { label: 'Evaluation Metric', value: 'Evaluation Metric' },
          { label: 'Material', value: 'Material' },
          { label: 'Other Scientific Terms', value: 'Other Scientific Terms' },
          { label: 'Generic', value: 'Generic' }
    ];

    const openEdgeDialog = () => {
      edgeDialogVisible.value = true;
      nextTick(() => {
        const edgeChartCtx = document.getElementById('edgeChart').getContext('2d');
        edgeChartInstance = new Chart(edgeChartCtx, {
          type: 'bar',
          data: {
            labels: Object.keys(edgeNameCounts.value),
            datasets: [
              {
                label: '边类型统计',
                data: Object.values(edgeNameCounts.value),
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
              },
            ],
          },
          options: {
            scales: {
              y: {
                beginAtZero: true,
              },
            },
          },
        });
      });
    };

    const handleConfirm_edge = () => {
      // 关闭模态框
      edgeDialogVisible.value = false;
      // 销毁图表
      if (edgeChartInstance) {
        edgeChartInstance.destroy();
        edgeChartInstance = null;
      }
    };

    const handleConfirm_node = () => {
      // 关闭模态框
      nodeDialogVisible.value = false;
      // 销毁图表
      if (nodeChartInstance) {
        nodeChartInstance.destroy();
        nodeChartInstance = null;
      }
    };

    // 查询所有结点（限定在当前paperName下）
    const query_all = async () => {
      const query = `MATCH (n {title: '${paperName.value}'})OPTIONAL MATCH (n)-[r]->(m) RETURN n, r, m`;
      return await executeQuery(query);
    };


    // 查询指定名称结点（限定在当前paperName下）
    const query_by_name_type = async () => {

      let query = '';

      if (query_name.value !== '' && (selectedType.value === 'all' || selectedType.value === '')) {
        query = `MATCH (n {title: '${paperName.value}', name: '${query_name.value}'})OPTIONAL MATCH (n)-[r]-(m) RETURN n, r, m`;
      }
      else if (query_name.value === '' && (selectedType.value !== 'all' && selectedType.value !== '')) {
        query = ` MATCH (n {title: '${paperName.value}', type: '${selectedType.value}'})OPTIONAL MATCH (n)-[r]-(m {type: '${selectedType.value}'})RETURN n, r, m`;
      }
      else if (query_name.value !== '' && (selectedType.value !== 'all' && selectedType.value !== '')) {
        query = `MATCH (n {title: '${paperName.value}', type: '${selectedType.value}', name: '${query_name.value}'})OPTIONAL MATCH (n)-[r]-(m) RETURN n, r, m`;
      }
      else {
        await update_KG(await query_all());
        return;
      }

      await update_KG(await executeQuery(query));
    };


    const node_get_color = ((type) => {
      if (type === 'Task') return '#FFB6C1'; // 类似浅红
      else if (type === 'Method') return '#ADD8E6'; // 浅蓝
      else if (type === 'Evaluation Metric') return '#90EE90'; // 浅绿
      else if (type === 'Material') return '#FFFFE0'; // 浅黄色
      else if (type === 'Other Scientific Terms') return '#DDA0DD'; // 浅紫
      else if (type === 'Generic') return '#FF8C00';   //浅橙色
      return 'gray'; // 默认颜色
    });

    const edge_get_color = ((type) => {
      if (type === 'Used-for') return 'red';
      else if (type === 'Feature-of') return 'blue';
      else if (type === 'Hyponym-of') return 'green';
      else if (type === 'Part-of') return 'yellow';
      else if (type === 'Compare') return 'purple';
      else if (type === 'Conjunction') return 'pink';
      return 'gray'; // 默认颜色
    });

    // 更新知识图谱
    const update_KG = async (records) => {
      if (!Array.isArray(records)) {
        console.error('records 不是一个数组:', records);
        return;
      }

      const nodes = [];
      const edges = [];

      records.forEach((record) => {
        const sourceNode = record.get('n');
        const targetNode = record.get('m'); // 可能为 null
        const relationship = record.get('r'); // 可能为 null

        // 添加源节点（一定存在）
        if (!nodes.some((node) => node.id === sourceNode.identity.low)) {
          nodes.push({
            id: sourceNode.identity.low,
            label: sourceNode.properties.name || sourceNode.labels[0],
            type: sourceNode.properties.type,
            color: node_get_color(sourceNode.properties.type),
          });
        }


        // 仅在有边和目标节点时，才添加它们
        if (targetNode) {
          if (!nodes.some((node) => node.id === targetNode.identity.low)) {
            nodes.push({
              id: targetNode.identity.low,
              label: targetNode.properties.name || targetNode.labels[0],
              type: targetNode.properties.type,
              color: node_get_color(targetNode.properties.type),
            });
          }
        }
        if (relationship){
          edges.push({
            from: sourceNode.identity.low,
            to: targetNode.identity.low,
            label: relationship.type,
            color: edge_get_color(relationship.type),
            arrows: 'to',
          });
        }
      });

      // 统计图数据
      nodeCount.value = nodes.length;
      edgeCount.value = edges.length;
      averageDegree.value = edgeCount.value > 0 ? (edgeCount.value) / nodeCount.value : 0;
      averageDegree.value = parseFloat(averageDegree.value.toFixed(1));

      edgeNameCounts.value = {};
      edges.forEach((edge) => {
        if (edgeNameCounts.value[edge.label]) {
          edgeNameCounts.value[edge.label]++;
        } else {
          edgeNameCounts.value[edge.label] = 1;
        }
      });

      nodeTypeCounts.value = {};
      nodes.forEach((node) => {
        const nodeType = node.type || 'Other Scientific Terms';
        if (nodeTypeCounts.value[nodeType]) {
          nodeTypeCounts.value[nodeType]++;
        } else {
          nodeTypeCounts.value[nodeType] = 1;
        }
      });

      const container = document.getElementById('network');
      const data = {
        nodes,
        edges,
      };
      const options = {};
      if (network.value != null) network.value.destroy();
      network.value = new Network(container, data, options);
    };

    const openNodeDialog = () => {
      nodeDialogVisible.value = true;
      nextTick(() => {
        const nodeChartCtx = document.getElementById('nodeChart').getContext('2d');
        nodeChartInstance = new Chart(nodeChartCtx, {
          type: 'bar',
          data: {
            labels: Object.keys(nodeTypeCounts.value),
            datasets: [
              {
                label: '结点类型统计',
                data: Object.values(nodeTypeCounts.value),
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
              },
            ],
          },
          options: {
            scales: {
              y: {
                beginAtZero: true,
              },
            },
          },
        });
      });
    };

    onMounted(async () => {
      const storedPapername = localStorage.getItem('paperName');
      if (storedPapername) paperName.value = storedPapername;
      await update_KG(await query_all());

    });

    onBeforeUnmount(() => {
      if (network.value) {
        network.value.destroy();
      }
    });

    const connectNodeA = ref('');
    const connectNodeB = ref('');
    const connectivityDialogVisible = ref(false);
    const connectivityResult = ref('');

    const checkConnectivity = async () => {
      if (!connectNodeA.value || !connectNodeB.value) {
        connectivityResult.value = '请填写两个节点名称';
        connectivityDialogVisible.value = true;
        return;
      }

      const query = `
    MATCH (a {title: '${paperName.value}', name: '${connectNodeA.value}'}),
          (b {title: '${paperName.value}', name: '${connectNodeB.value}'})
    RETURN EXISTS((a)-[*]-(b)) AS connected
  `;


      try {
        const result = await executeQuery(query);
        if (result.length > 0 && result[0].get('connected')) {
          connectivityResult.value = `节点 "${connectNodeA.value}" 和 "${connectNodeB.value}" 是连通的。`;
        } else {
          connectivityResult.value = `节点 "${connectNodeA.value}" 和 "${connectNodeB.value}" 不连通。`;
        }
      } catch (error) {
        console.error('连通性查询失败:', error);
        connectivityResult.value = '查询过程中出现错误';
      }

      connectivityDialogVisible.value = true;
    };


    return {
      edgeDialogVisible,
      nodeDialogVisible,
      openEdgeDialog,
      openNodeDialog,
      network,
      nodeCount,
      edgeCount,
      averageDegree,
      edgeNameCounts,
      nodeTypeCounts,
      handleConfirm_edge,
      handleConfirm_node,
      query_name,
      query_by_name_type,
      selectedType,
      nodeTypes,
      connectNodeA,connectNodeB,checkConnectivity,connectivityDialogVisible,connectivityResult
    };
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


.connectivity-check {
  background-color: rgba(255, 255, 255, 0.8); /* 设置背景颜色和透明度 */
  display: flex;
  position: absolute;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 10px;
  top: 650px;
}

#network {
  width: 100%;
  height: 600px;
}

canvas {
  margin-top: 20px;
}
.graph-container {
  position: relative; /* 设置父容器为相对定位 */
}

.indicator-box {
  position: absolute; /* 设置指标显示框为绝对定位 */
  top: 0px;
  left: 0;
  background-color: rgba(255, 255, 255, 0.8); /* 设置背景颜色和透明度 */
  padding: 10px;
  border-radius: 5px;
  z-index: 1; /* 设置 z-index 确保显示在上方 */
}

.button-group {
  position: absolute; /* 设置按钮组为绝对定位 */
  top: 10px;
  right: 10px;
  z-index: 1; /* 设置 z-index 确保显示在上方 */
}

.search-box {
  position: absolute;
  top: 10px;
  left: 200px;
  transform: translateX(-50%);
  margin-bottom: 20px;
  z-index: 1;
  /* 为了方便查看效果，添加一些样式 */
  width: 150px;
  height: 30px;
  background-color: lightblue;
  opacity: 0.9;
}

.xiala {
  position: absolute;
  top: 10px;
  left: 325px;
  transform: translateX(-50%);
  margin-bottom: 20px;
  z-index: 1;
  /* 为了方便查看效果，添加一些样式 */
  width: 100px;
  height: 30px;
  background-color: lightblue;
  opacity: 0.9;
}

#network {
  width: 100%;
  height: 730px; /* 设置知识图谱容器的高度 */
}
</style>