package com.example.bishe.entity_reco;

import com.example.bishe.SpringContextUtil.SpringContextUtil;
import com.example.bishe.dealWithPdf.ReadPDF;
import com.example.bishe.ollama.aiInter;
import com.example.bishe.service.NodeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entity_Relation_reco {
    String location;
    public List<String> sentences;
    NodeService nodeService;
    aiInter _aiInter;
    String model;
    String title;

    public String question1_1 = "【待提取实体的句子】\n";

    public String question1_2 = "\n\n【待提取实体的句子】\n" +
            "\n" +
            "\n" +
            "【实体类型定义】\n" +
            "Task | 应用目标、待解决的科研问题、需构建的系统\n" +
            "示例：图像分割、机器阅读理解系统、蛋白质结构预测\n" +
            "\n" +
            "Method | 具体技术方案、工具、算法、框架、系统组件\n" +
            "示例：BERT、CoreNLP工具包、词性标注模块、随机森林算法\n" +
            "\n" +
            "Evaluation Metric | 量化评估指标或抽象质量属性\n" +
            "示例：F1值、时间复杂度、鲁棒性、ROC曲线\n" +
            "\n" +
            "Material | 数据集、知识库、实验材料\n" +
            "示例：CoNLL-2003数据集、ImageNet图像库、维基百科语料\n" +
            "\n" +
            "Other Scientific Terms | 领域专业术语（不属于上述四类）\n" +
            "示例：几何约束、句法规则、语义树、噪声过滤\n" +
            "\n" +
            "Generic | 通用词汇或代词（信息性弱）\n" +
            "示例：模型、方法、它们、该算法\n" +
            "\n" +
            "【提取规则优化】\n" +
            "分类细化：\n" +
            "Method vs Material：工具/框架属于 Method，其使用的数据资源属于 Material\n" +
            "正确示例：\n" +
            "CoreNLP工具包:Method（工具） vs 宾夕法尼亚树库:Material（数据集）\n" +
            "\n" +
            "Metric 扩展：包含抽象质量属性（如鲁棒性:Evaluation Metric）\n" +
            "\n" +
            "Generic 严格化：仅当无法关联具体实体时使用（如模型:Generic）\n" +
            "\n" +
            "优先级调整：\n" +
            "若名词短语同时符合多类，按 Task > Method > Evaluation Metric > Material > Other Scientific Terms > Generic 选择\n" +
            "\n" +
            "格式一致性：\n" +
            "与关系抽取对齐，保留原文大小写和符号\n" +
            "实体与实体间使用中文逗号分隔,实体和关系的冒号用英文冒号，如\"实体1:关系，实体2:关系\"\n" +
            "错误示例：[F1-score:Metric] ❌（类型名必须完整）\n" +
            "正确示例：[F1-score:Evaluation Metric] ✅\n" +
            "\n" +
            "【示例】\n" +
            "示例1（区分 Method/Material）\n" +
            "输入：\n" +
            "\"使用 CoreNLP 工具处理宾夕法尼亚树库中的句法树。\"\n" +
            "输出：\n" +
            "结果是[CoreNLP:Method，宾夕法尼亚树库:Material，句法树:Other Scientific Terms]\n" +
            "\n" +
            "示例2（Metric 扩展）\n" +
            "输入：\n" +
            "\"对比模型的鲁棒性和时间复杂度，结果优于基线 2%。\"\n" +
            "输出：\n" +
            "结果是[鲁棒性:Evaluation Metric，时间复杂度:Evaluation Metric，基线:Generic]\n" +
            "\n" +
            "示例3（Generic 严格过滤）\n" +
            "输入：\n" +
            "\"该方法整合了多种模型，它们均通过并行计算加速。\"\n" +
            "输出：\n" +
            "结果是[该方法:Generic，模型:Generic，并行计算:Other Scientific Terms]\n" +
            "\n" +
            "【禁止行为强化】\n" +
            "禁止过度泛化：\n" +
            "如 深度学习:Generic ❌ → 应归类为 Other Scientific Terms ✅\n" +
            "\n" +
            "禁止工具与数据混淆：\n" +
            "PyTorch框架:Method ✅ vs CIFAR-10数据集:Material ✅\n" +
            "\n" +
            "禁止拆分短语：\n" +
            "错误示例：[机器:Generic, 翻译:Generic] ❌\n" +
            "正确示例：[机器翻译:Task] ✅\n" +
            "\n" +
            "【最终输出格式】\n" +
            "结果是[实体:类型，实体:类型，...]";



    public String question2_1 = "【给定句子及实体】\n";

    public String question2_2 = "\n\n【关系类型及定义】\n" +
            "关系类型\t方向\t定义与模式\n" +
            "Used-for\tA → B\tB 用于实现/支撑/优化 A（如方法用于任务，数据用于训练）\n" +
            "Feature-of\tB → A\tB 是 A 的特征/属性（如技术特征属于模型）\n" +
            "Hyponym-of\tB → A\tB 是 A 的子类/实例（如具体模型是某框架的实现）\n" +
            "Part-of\tB → A\tB 是 A 的组成部分（如模块属于系统）\n" +
            "Compare\tA ↔ B\tA 与 B 被直接比较或对立（对称关系）\n" +
            "Conjunction\tA ↔ B\tA 与 B 并列使用或协同作用（对称关系）\n" +
            "【抽取规则】\n" +
            "优先级：\n" +
            "优先抽取明确的关系（如动词/介词提示），其次依赖领域常识\n" +
            "若实体间存在多关系，按优先级 Used-for > Part-of > Hyponym-of > Feature-of > Compare/Conjunction 选择\n" +
            "\n" +
            "方向性：\n" +
            "非对称关系（如 Used-for）需区分方向，格式为 A->B：关系类型\n" +
            "对称关系（如 Compare）用 A->B：关系类型 表示双向关系\n" +
            "\n" +
            "格式要求：\n" +
            "实体必须与之前提取的实体完全一致（保留大小写/符号）\n" +
            "多个关系用中文逗号分隔，使用的冒号为中文冒号\n" +
            "错误示例：BERT-based NER system:Used-for:Task ❌\n" +
            "正确示例：BERT-based NER system->image segmentation：Used-for ✅\n" +
            "\n" +
            "【示例】\n" +
            "示例1\n" +
            "输入：\n" +
            "给定句子：\"The BERT-based NER system achieves 92% F1 on CoNLL-2003.\"\n" +
            "给定实体：[BERT-based NER system:Method, F1:Metric, CoNLL-2003:Material]\n" +
            "关系输出：\n" +
            "结果是[BERT-based NER system->image segmentation：Used-for，CoNLL-2003->BERT-based NER system：Used-for]\n" +
            "\n" +
            "示例2\n" +
            "输入：\n" +
            "给定句子：\"物理约束和几何先验知识提升分割精度。\"\n" +
            "给定实体：[物理约束:OtherScientificTerm, 几何先验知识:OtherScientificTerm, 分割精度:Metric]\n" +
            "关系输出：\n" +
            "结果是[物理约束->几何先验知识：Conjunction，物理约束->分割精度：Feature-of，几何先验知识->分割精度：Feature-of]\n" +
            "\n" +
            "示例3\n" +
            "输入：\n" +
            "给定句子：\"与基于规则的模型不同，神经网络方法显著提高了鲁棒性。\"\n" +
            "给定实体：[基于规则的模型:Method, 神经网络方法:Method, 鲁棒性:Metric]\n" +
            "关系输出：\n" +
            "结果是[基于规则的模型->神经网络方法：Compare，神经网络方法->鲁棒性：Used-for]\n" +
            "\n" +
            "示例4\n" +
            "输入：\n" +
            "给定句子：\"自然语言处理模块包含词性标注和依存句法分析两个组件。\"\n" +
            "给定实体：[自然语言处理模块:Method, 词性标注:Method, 依存句法分析:Method]\n" +
            "关系输出：\n" +
            "结果是[词性标注->自然语言处理模块：Part-of，依存句法分析->自然语言处理模块：Part-of]\n" +
            "\n" +
            "【禁止行为】\n" +
            "不添加未在原文中明确出现的关系\n" +
            "不修改实体名称（如缩写展开、大小写转换）\n" +
            "不确定关系方向时，优先选择 Conjunction 或 Compare\n" +
            "禁止合并多个关系（如 A->B，Used-for & Part-of ❌）\n" +
            "\n" +
            "【输出格式】\n" +
            "结果是[实体A->后实体B：关系，前实体C->后实体D：关系...]";

    int option;


    //构造方法，从spring容器中获取service实例
    public Entity_Relation_reco(String Location,String model,String title){
        this.location = Location;
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.model = model;
        this.title = title;
        option = 0;
        getSentences();
    }

    public Entity_Relation_reco(String Location,String title){
        this.location = Location;
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.title = title;
        option = 1;
        getSentences();
    }


    public void main(){
        int n = sentences.size();
        for (int i=0;i<n;i++){
            getText(sentences.get(i),i);
        }
        System.out.println("文献创建Neo4J知识图谱完成");
    }

    //读取pdf内容并预处理
    private void getSentences(){
        this.sentences = ReadPDF.deal(location);
    }



    //通过AI得到文本回答
    public void getText(String sentence,int i){
        System.out.println("提取第"+i+"句话中实体："+sentence);
        String text;
        if (option == 0) text = _aiInter.tiwen(sentence+question1_1,0.1F,model);
        else text = _aiInter.tiwen(question1_1+sentence+question1_2);
        getEntity(text,sentence);
    }

    //从文本回答中提取实体
    public void getEntity(String text, String sentence) {
        // 定义正则表达式，用于匹配结果是后面方括号里的内容
        String regex = "结果是\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        List<String> entities = new ArrayList<>();
        List<String> types = new ArrayList<>();

        if (matcher.find()) {
            // 获取方括号内的内容
            String entityString = matcher.group(1);
            // 按逗号分割内容
            String[] entityTypePairs = entityString.split("，");
            for (String pair : entityTypePairs) {
                // 按冒号分割实体和类型
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    entities.add(parts[0].trim());
                    types.add(parts[1].trim());
                }
            }
        }
        System.out.println("提取完成");
        System.out.println("实体为：" + entities);
        System.out.println("类型为：" + types);
        System.out.println("添加实体中...");
        for (int i=0;i< entities.size();i++) nodeService.createEntity(entities.get(i),types.get(i),title);
        // 假设这里的 getRelation 方法接收实体列表和句子作为参数
        getRelation(entities.toString(), sentence);
    }


    //向ai提问获取实体之间关系
    public void getRelation(String entities,String sentence){
        Pattern pattern;
        Matcher matcher;
        while(true) {
            try {
                String text;
                if (option == 0) text = _aiInter.tiwen(question2_1+"给定句子："+sentence+"\n给定实体："+entities+question2_2, 0.1F, model);
                else text = _aiInter.tiwen(question2_1+"给定句子："+sentence+"\n给定实体："+entities+question2_2);
                String regex = "结果是\\[(.*?)\\]";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(text);
                break;
            }
        catch (NullPointerException e){
                System.out.println("模型回答格式错误，重新提问");
            }
        }


        List<String> relations = new ArrayList<>();
        if (matcher.find()) {
            // 获取方括号内的内容
            String entityString = matcher.group(1);
            // 按逗号分割内容
            String[] entityArray = entityString.split("，");
            for (String entity : entityArray) {
                relations.add(entity.trim());
            }
        }

        String name1,name2,relation;
        System.out.println("关系为："+relations);
//        System.out.println(text);

        // 循环处理 relations 列表中的每个元素
        System.out.println("Neo4J添加关系中...");
        for (String entity : relations) {
            // 按 -> 分割字符串
            String[] parts = entity.split("->");
            if (parts.length == 2) {
                name1 = parts[0];
                // 再按 ：分割第二部分
                String[] relationParts = parts[1].split("：");
                if (relationParts.length == 2) {
                    name2 = relationParts[0];
                    relation = relationParts[1];
                    // 这里可以对 name1, name2, relation 进行进一步操作，例如打印或存储
                    buildNeo4j_Relation(name1,name2,relation);
                }
            }
        }
        System.out.println("添加完成\n");
    }

    //创建neo4j关系
    public void buildNeo4j_Relation(String name1,String name2,String relation){
        nodeService.createRelationBetweenNodes(name1,name2,relation,title);
    }




}
