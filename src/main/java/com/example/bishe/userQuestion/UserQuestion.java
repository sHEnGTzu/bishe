package com.example.bishe.userQuestion;

import com.example.bishe.SpringContextUtil.SpringContextUtil;
import com.example.bishe.entity.entity;
import com.example.bishe.ollama.aiInter;
import com.example.bishe.service.NodeService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserQuestion {

    NodeService nodeService;
    aiInter _aiInter;
    String model;

    String target_question;

    String question1_1 = "\n"+"你需要从以上句子中准确提取具有独立语义的实体。请严格遵循以下流程：\n" +
            "\n" +
            "<处理流程>\n" +
            "1. 名词识别阶段：\n" +
            "   逐词扫描句子，标记所有名词和名词性短语\n" +
            "   包含专有名词、术语、概念名称等\n" +
            "   注意识别复合名词（如\"医案信息模型\"）\n" +
            "\n" +
            "2. 语义筛选阶段：\n" +
            "   判断每个候选名词是否满足：\n" +
            "   a) 具有独立完整的语义内涵\n" +
            "   b) 不依赖其他实体才能形成明确含义\n" +
            "   c) 在上下文语境中代表具体事物/概念\n" +
            "   排除标准：\n" +
            "   - 仅描述其他实体特征（如\"演变历程\"）\n" +
            "   - 作为组成部分存在（如\"结构要素\"）\n" +
            "   - 需要组合其他词才有独立意义\n" +
            "\n" +
            "3. 验证阶段：\n" +
            "   检查提取结果是否满足：\n" +
            "   - 每个实体都能独立对应现实概念\n" +
            "   - 没有遗漏重要实体\n" +
            "   - 没有包含描述性成分\n" +
            "</处理流程>\n" +
            "\n" +
            "<示例参考>\n" +
            "输入：\"本研究探索基于大语言模型的医案命名实体的自动化抽取过程\"\n" +
            "分析：排除\"过程\"（描述性成分），保留\"大语言模型\"、\"医案命名实体\"、\"自动化抽取\"\n" +
            "结果：[大语言模型，医案命名实体，自动化抽取]\n" +
            "</示例参考>\n" +
            "\n" +
            "请先在<分析>标签中执行以下操作：\n" +
            "1. 列出所有识别出的候选名词\n" +
            "2. 说明每个候选名词的筛选理由\n" +
            "3. 验证最终实体列表的完整性\n" +
            "\n" +
            "最终严格按照\"结果是[实体1，实体2，...]\"的格式输出，保持实体原词形态，不要任何解释性文字。确保：\n" +
            "- 使用中文方括号\n" +
            "- 实体间用全角逗号分隔\n" +
            "- 保留原始大小写和缩写形式";


    String question2_1 = "### 任务说明\n" +
            "你是一个回答问题的客服，需要根据所给的知识图谱信息，回答用户的相关问题，回答时可以结合自己已知的知识和知识图谱的信息。\n" +
            "\n" +
            "### 操作步骤\n" +
            "1. **理解知识图谱信息**：仔细阅读知识图谱信息并理解。\n" +
            "2. **理解用户问题**：认真分析并理解用户所提出的问题。\n" +
            "3.**组织并给出答案**：结合知识图谱信息和自己已知的知识，根据用户问题组织并给出最终答案。\n" +
            "\n" +
            "### 示例\n" +
            "{\n" +
            "知识图谱信息：[知识图谱->支持->可微推理, 结构化知识->补全->知识图谱,推理 ->组成->知识图谱]\n" +
            "用户问题：知识图谱是用来干嘛的？\n" +
            "思考过程：首先，理解知识图谱：知识图谱支持可微推理，并可以被结构化知识补全，也可被推理所组成；其次，理解用户问题：用户想要知道知识图谱的一般用途或其余可能用途；最后，结合自己所知的知识，组织并给出答案：知识图谱可以存储事实，存储实体-关系三元组，支持可微推理，由结构化知识和推理组成。\n" +
            "回答：知识图谱可以存储事实，存储实体-关系三元组，支持可微推理，由结构化知识和推理组成。\n" +
            "}\n" +
            "{\n" +
            "知识图谱信息：[图像识别->应用领域->安防监控, 深度学习->技术基础->图像识别, 安防监控->需求推动->图像识别]\n" +
            "用户问题：图像识别在哪些方面有应用？\n" +
            "思考过程：首先，理解知识图谱：图像识别以深度学习为技术基础，在安防监控领域有应用，且安防监控的需求推动了图像识别的发展；其次，理解用户问题：用户想了解图像识别的应用方面；最后，结合自己所知的知识，组织并给出答案：图像识别在安防监控等领域有应用，同时在实际中图像识别还广泛应用于交通、医疗、金融等众多领域。\n" +
            "回答：图像识别在安防监控等领域有应用，同时在实际中图像识别还广泛应用于交通、医疗、金融等众多领域。\n" +
            "}\n" +
            "\n" +
            "### 重要的知识图谱信息及待回答问题\n" +
            "知识图谱信息：";

    String question2_2 = "\n用户问题：";

    public UserQuestion(String question,String model){
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.model = model;
        this.target_question = question;
    }

    public String main(){
        String result = _aiInter.tiwen(target_question+question1_1,0.1f,model);

        // 定义正则表达式，用于匹配结果是后面方括号里的内容
        String regex = "结果是\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);

        List<String> entities = new ArrayList<>();
        if (matcher.find()) {
            // 获取方括号内的内容
            String entityString = matcher.group(1);
            // 按逗号分割内容
            String[] entityArray = entityString.split("，");
            for (String entity : entityArray) {
                entities.add(entity.trim());
            }
        }

        List<String> entities_relations_triple = new ArrayList<>();
        entity temp;
        for (String entity : entities) {
            temp = nodeService.findNodeByName(entity);
            if (temp != null) {
                for (int j = 0; j < temp.getRelation().size(); j++) {
                    entities_relations_triple.add(temp.getName() + "->" + temp.getRelation().get(j).getRelation() + "->" + temp.getRelation().get(j).getEntity().getName());
                }
            }
        }

        return questionWithInfo(entities_relations_triple);
    }

    public String questionWithInfo(List<String> entities_relations_triple){
        return _aiInter.tiwen(question2_1+entities_relations_triple.toString()+question2_2+target_question,0.3f,model);
    }


}
