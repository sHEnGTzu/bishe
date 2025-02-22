package com.example.bishe.entity_reco;

import com.example.bishe.SpringContextUtil.SpringContextUtil;
import com.example.bishe.dealWithPdf.ReadPDF;
import com.example.bishe.ollama.aiInter;
import com.example.bishe.service.NodeService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entity_Relation_reco {
    String location;
    public List<String> sentences;
    NodeService nodeService;
    aiInter _aiInter;
    String model;

    public String question1_1 = "### 任务说明\n" +
            "你需要从下面给出的重要待提取句子中准确、完整地提取出所有实体。为了帮助你更好地完成任务，我会提供一些示例，你可以学习示例中的思考过程，但最终只需输出提取结果。\n" +
            "\n" +
            "### 提示\n" +
            "在提取实体时，先找出句子中的所有名词，然后根据实际语义判断这些名词是否能独立作为一个有意义的实体。对于一些在句子中仅用于描述其他实体特征或组成部分，且不具备独立语义的名词，可以不作为实体提取。\n" +
            "\n" +
            "### 示例\n" +
            "{\n" +
            "句子：知识图谱（knowledge graph，KG）通过由实体和关系组成的三元组（头实体，关系，尾实体）来存储大量事实，例如经典的通用知识图谱YAGO（yetanother great ontology）[1]、DBpedia[2]和 Wikidata[3]等。\n" +
            "思考过程：首先，找出句子中的名词有知识图谱、实体、关系、三元组、头实体、尾实体、事实、通用知识图谱、YAGO、DBpedia、Wikidata。然后分析，头实体和尾实体是用来描述三元组具体组成形式的，它们本身不具有独立于三元组之外的明确语义，所以不算作实体。而知识图谱、实体、关系、三元组、事实、通用知识图谱、YAGO、DBpedia、Wikidata都具有独立的语义和明确的指代，因此可以作为实体。\n" +
            "结果：结果是[知识图谱，实体，关系，三元组，事实，通用知识图谱，YAGO，DBpedia，Wikidata]\n" +
            "}\n" +
            "{\n" +
            "句子：Moreover, our investigation extends to the potential generalization ability of LLMs for information extraction, leading to the proposition of a Virtual Knowledge Extraction task and the development of the corresponding VINE dataset.\n" +
            "思考过程：第一步，找出句子里的名词有generalization ability、LLMs、information extraction、Virtual Knowledge Extraction task、corresponding VINE dataset。接着判断，这些名词都具有独立的语义，在句子中分别代表不同的概念或事物，所以都能算作实体。\n" +
            "结果：结果是[generalization ability，LLMs，information extraction，Virtual Knowledge Extraction task，corresponding VINE dataset]\n" +
            "}\n" +
            "\n" +
            "### 重要的待提取的句子\n";

    public String question1_2 = "\n"+"\n"+"### 回答格式\n" +
            "请严格按照“结果是[实体1，实体2，......]”的格式输出提取结果。不要用json格式返回。";

    public String question2_1 = "### 任务说明\n" +
            "你要基于给定的段落，全面且不遗漏地找出待定实体列表里各实体相互之间可能存在的关系。需注意，关系的描述格式为前面实体对于后面实体的关系。\n" +
            "\n" +
            "### 提示\n" +
            "在识别实体关系时，按以下步骤操作：\n" +
            "1. 精准定位每个实体在句子中的位置。\n" +
            "2. 针对该位置所在的句子，仔细分析判断其中其他实体与该实体是否存在关系。\n" +
            "3. 若在该句子内未发现关系，可尝试以该句子为中心，逐步扩大范围（如上下相邻句子等，本任务仅围绕给定的这一个句子）来寻找潜在关系。\n" +
            "4. 关系类型可能多样，包括但不限于组成、来源、使用、关联、目的等，要充分考虑句子语义来确定合适的关系表述。\n" +
            "\n" +
            "### 示例\n" +
            "{\n" +
            "句子：知识图谱（knowledge graph，KG）通过由实体和关系组成的三元组（头实体，关系，尾实体）来存储大量事实。\n" +
            "给定实体：[知识图谱，实体，关系，三元组，事实]\n" +
            "思考过程：首先，分别定位各实体在句子中的位置。对于“知识图谱”，在其所在句子里，分析其他实体与它的关系，发现它由“实体”和“关系”组成，并且它是“三元组”的集合形式；对于“三元组”，可知它用来“存储”“事实”，同时它由“实体”和“关系”组成。\n" +
            "结果：结果是[实体-知识图谱：组成，关系-知识图谱：组成，三元组-事实：存储，三元组-实体：组成，三元组-关系：组成]\n" +
            "}\n" +
            "{\n" +
            "句子：Moreover, our investigation extends to the potential generalization ability of LLMs for information extraction.\n" +
            "给定实体：[generalization ability，LLMs，information extraction]\n" +
            "思考过程：第一步，明确每个实体在句子中的位置。接着，依据句子语义判断实体间关系，发现“generalization ability”是“LLMs”所具有的一种能力，即“属于”关系；“LLMs”在句子中是用于“information extraction”这一活动的。\n" +
            "结果：结果是[generalization ability-LLMs：属于，LLMs-information extraction：用于]\n" +
            "}\n" +
            "\n" +
            "### 重要的待提取的句子及实体\n" +
            "句子：";

    public String question2_2 = "\n" +
            "### 回答格式\n" +
            "请严格依照“结果是[实体1-实体2：关系，实体3-实体4：关系，......]”的固定格式输出提取结果，不要用json格式返回。";



    //构造方法，从spring容器中获取service实例
    public Entity_Relation_reco(String Location,String model){
        this.location = Location;
        nodeService = SpringContextUtil.getBean(NodeService.class);
        _aiInter = new aiInter();
        this.model = model;
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
        String text = _aiInter.tiwen(question1_1+sentence+question1_2,0.4F,model);
        getEntity(text,sentence);
    }

    //从文本回答中提取实体
    public void getEntity(String text,String sentence){
        // 定义正则表达式，用于匹配结果是后面方括号里的内容
        String regex = "结果是\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

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
        System.out.println("提取完成");
        System.out.println("实体为："+ entities);
        getRelation(entities.toString(),sentence);
    }


    //向ai提问获取实体之间关系
    public void getRelation(String entities,String sentence){
        Pattern pattern;
        Matcher matcher;
        while(true) {
            try {
                String text = _aiInter.tiwen(question2_1 + sentence + "\n给定实体：" + entities + "\n" + question2_2, 0.3F, model);
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
            // 按 - 分割字符串
            String[] parts = entity.split("-");
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
        System.out.println("添加完成");
    }

    public void buildNeo4j_Relation(String name1,String name2,String relation){
        nodeService.createRelationBetweenNodes(name2,name1,relation);
    }




}
