package com.example.bishe.userQuestion;

import com.example.bishe.SpringContextUtil.SpringContextUtil;
import com.example.bishe.entity.entity;
import com.example.bishe.ollama.aiInter;
import com.example.bishe.service.NodeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {

    NodeService nodeService;
    aiInter _aiInter;
    String model;

    String target_question;

    String question1_1 = """
            你是一个知识图谱实体提取专家，你的任务是从用户的问题中提取出用户希望在知识图谱中查询的“节点实体”。
                        
            请遵循以下要求：
                        
            1. 只提取具有实际语义的节点名称或术语；
            2. 不提取动词、虚词、连接词或问题结构（如“是否”、“存在”等）；
            3. 保留实体在原句中的原始词形、大小写与缩写形式；
            4. 输出必须严格按照以下格式返回：
               [实体1，实体2，...]
            5. 输出中禁止出现任何解释性文字、空格或换行，仅返回格式化结果。
                        
            请处理以下用户问题：
                        
            用户问题：%s\040\040
            """;


    String question2_1 = "### 任务说明\n" +
            "你是一个回答问题的客服，需要根据所给的知识图谱信息，回答用户对于知识图谱结构的相关问题。\n" +
            "\n" +
            "### 重要的知识图谱信息及待回答问题\n" +
            "知识图谱信息：";

    String question2_2 = "\n用户问题：";

    public Chat(String question, String model){
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.model = model;
        this.target_question = question;
    }

    public Chat(String question){
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.target_question = question;
    }

    public String main(){
//        String result = _aiInter.tiwen(target_question+question1_1,0.1f,model);
        System.out.println(question1_1.formatted(target_question));
        String result = _aiInter.tiwen(question1_1.formatted(target_question));
        System.out.println(result);
        // 定义正则表达式，用于匹配结果是后面方括号里的内容
        String regex = "\\[(.*?)\\]";
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

    public String allNodesQuestion(String papername){
        List<String> entities_relations_triple = new ArrayList<>();

        // 查询所有节点之间的关系
        List<Map<String, String>> triples = nodeService.getAllTriples(papername);

        for (Map<String, String> triple : triples) {
            String start = triple.get("start");
            String relation = triple.get("relation");
            String end = triple.get("end");
            entities_relations_triple.add(start + "->" + relation + "->" + end);
        }
        return questionWithInfo(entities_relations_triple);
    }

    public String questionWithInfo(List<String> entities_relations_triple){
        System.out.println(entities_relations_triple);
        return _aiInter.tiwen(question2_1+entities_relations_triple.toString()+question2_2+target_question);
    }


}
