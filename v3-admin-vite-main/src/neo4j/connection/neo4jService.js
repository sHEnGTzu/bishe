import neo4j from 'neo4j-driver';

// 配置 Neo4j 连接信息
const driver = neo4j.driver(
  'bolt://localhost:7687', // Neo4j 连接地址
  neo4j.auth.basic('neo4j', '123456') // 用户名和密码
);

// 执行 Cypher 查询的函数
export async function executeQuery(query) {
  const session = driver.session();
  try {
    const result = await session.run(query);
    return result.records;
  } finally {
    await session.close();
  }
}
