package com.tengfei.bigmodel;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BigmodelApplicationTests {

    /**
     * 智谱客户端
     *
     * @return
     */
    public ClientV4 getClient() {
        ClientV4 client = new ClientV4.Builder("50fdb6a04fe8b8243ff00e97b28dc4ae.01t4uzjxs7nQphS8")

                .build();
        return client;
    }

    public ModelApiResponse execute(String template, Map<String, Object> data) {
        ClientV4 client = getClient();

        String msgTemplate = "通过模板、数据、条件判断生成文本，优化标点符号，不生成解释。模板：%s；数据：%s";
        String content = String.format(msgTemplate, template, JSON.toJSONString(data));

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), content);

        messages.add(chatMessage);

        String requestId = UUID.fastUUID().toString();
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp;
    }

    @Test
    void productCagrGrowthCnTest() {
        String template = "处于成长期的有${countryCount}个国家，<if ${array!=null}>各大洲的贸易平均增长率分别为：${array.stateName} ${array.stateTrend}，；</if>其中贸易增长率最高的国家为${maxCountry}，对${productName}的贸易增长率高达${maxTrend}";
        Map<String, Object> data = new HashMap<>();
        data.put("countryCount", 1);
        data.put("maxCountry", "中国");
        data.put("productName", "红霉素");
        data.put("maxTrend", "253.21%");
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "亚洲");
            put("stateTrend", "210.62%");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "非洲");
            put("stateTrend", "184.62%");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "北美洲");
            put("stateTrend", "26.12%");
        }});

//        data.put("array", dataList);
        System.out.println(JSON.toJSONString(data));
        ModelApiResponse response = execute(template, data);
        System.out.println(JSON.toJSONString(response));
        Choice choice = response.getData().getChoices().get(0);
        System.out.println("message = " + choice.getMessage().getContent());
    }

    @Test
    void productCagrGrowthEnTest() {
        String template = "There are ${countryCount} countries in a growth phase.<if ${array!=null}> The average trade growth rates for each continent are as follows: ${array.stateName} ${array.stateTrend},.</if> Among them, ${maxCountry} has the highest trade growth rate, with an ${productName} trade growth rate of ${maxTrend}.";
        Map<String, Object> data = new HashMap<>();
        data.put("countryCount", 12);
        data.put("maxCountry", "China");
        data.put("productName", "erythromycin");
        data.put("maxTrend", "253.21%");
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "Asia");
            put("stateTrend", "210.62%");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "Africa");
            put("stateTrend", "184.62%");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "North America");
            put("stateTrend", "26.12%");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "South America");
            put("stateTrend", "6.12%");
        }});

//        data.put("array", dataList);
        System.out.println(JSON.toJSONString(data));
        ModelApiResponse response = execute(template, data);
        System.out.println(JSON.toJSONString(response));
        Choice choice = response.getData().getChoices().get(0);
        System.out.println("message = " + choice.getMessage().getContent());
    }

    @Test
    void productCagrMaturityCnTest() {
        String template = "处于成熟期的有${countryCount}个国家，其中${array.stateName}有${array.countryCount}个国家，";
        Map<String, Object> data = new HashMap<>();
        data.put("countryCount", 12);
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "亚洲");
            put("countryCount", "12");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "非洲");
            put("countryCount", "10");
        }});

        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "北美洲");
            put("countryCount", "5");
        }});
        dataList.add(new HashMap<String, Object>() {{
            put("stateName", "南美洲");
            put("countryCount", "2");
        }});

        data.put("array", dataList);
        System.out.println(JSON.toJSONString(data));
        ModelApiResponse response = execute(template, data);
        System.out.println(JSON.toJSONString(response));
        Choice choice = response.getData().getChoices().get(0);
        System.out.println("message = " + choice.getMessage().getContent());
    }

    @Test
    void tradeFlowCnTest() {
        String template = "全球${productName}出口最多的大洲是${max.stateName}，贸易次数${max.stateCount}次，占比${max.stateRadio}；<if ${array!=null}>其次为：${array[0...].stateName}（贸易次数${array[0...].stateCount}次，占比${array[0...].stateRadio}）、和${array.stateName}（贸易次数${array.stateCount}次，占比${array.stateRadio}）。</if>";
        Map<String, Object> data = new HashMap<>();

        data.put("productName", "ipad");

        data.put("max", new HashMap<String, Object>() {{
            put("stateName", "亚洲");
            put("stateCount", "$2550");
            put("stateRadio", "25.5%");
        }});

        List<Map<String, Object>> dataList = new ArrayList<>();
//        dataList.add(new HashMap<String, Object>() {{
//            put("stateName", "北美洲");
//            put("stateCount", "$1805");
//            put("stateRadio", "21.04%");
//        }});
//
//        dataList.add(new HashMap<String, Object>() {{
//            put("stateName", "南美洲");
//            put("stateCount", "$1408");
//            put("stateRadio", "25.5%");
//        }});
//
//        dataList.add(new HashMap<String, Object>() {{
//            put("stateName", "非洲");
//            put("stateCount", "$1002");
//            put("stateRadio", "5.9%");
//        }});
//
//        dataList.add(new HashMap<String, Object>() {{
//            put("stateName", "欧洲");
//            put("stateCount", "$550");
//            put("stateRadio", "2.9%");
//        }});

        data.put("array", dataList);
        System.out.println(JSON.toJSONString(data));
        ModelApiResponse response = execute(template, data);
        System.out.println(JSON.toJSONString(response));
        Choice choice = response.getData().getChoices().get(0);
        System.out.println("message = " + choice.getMessage().getContent());
    }

}
