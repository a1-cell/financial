package com.imooc.activitiweb.controller;

import com.imooc.activitiweb.SecurityUtil;
import com.imooc.activitiweb.mapper.ActivitiMapper;
import com.imooc.activitiweb.pojo.RequestServiceEntity;
import com.imooc.activitiweb.util.AjaxResponse;
import com.imooc.activitiweb.util.GlobalConfig;
import com.imooc.activitiweb.util.HttpUtils;
import com.imooc.activitiweb.util.RSAUtils;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    ActivitiMapper mapper;

    static public String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiYdIEoyNOP7Fmhc4kAiam0SPGaPKNGb34OnxpT1AWjJgPexLgjdVQpBPmcb3PBNhYgXpxRMjswFmkavt/KJ24H5te21RlbnwCRxiQmQUhFPze1Scp+yXtO54p9gacmcSjFA4E8tXkgFf5Uyp9vJbTVg0xVX7XmdmM9Z72w7qULwIDAQAB";
    static public String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJUQxCubdAR2KBqI/e4IQQSsKW8bw5I1eh/EBI0wzDqcII4VPZGLvEpZlnGr99DzzKlQ1Lq8q/oluj45SwgDtQpfhy6Qx/sQStS/U0FZEgk8zy4kg9iJceEmFlUAkPDGf+myqe85MlXQ+UnenE88kwLMC49RAZvaPTcW2tZMtsw1AgMBAAECgYB67mPJTp8kGslB8N3xEq+ECfL1pWDoz99SGRZj1e1nHGCgrBWlVSbH7g/q2JY7pQOQUo/NHFF7rudj713YUy5WBCQcuYJw+CGk1Mw8SPo4x2ajVOcALYMh3gwXegI5es8P42xCfzC++96g0JN7pMDaGblSL4P8WS7A/JT1qgk/4QJBAOYGylhyOLnvsNa5dv89iM/MlXy1EWPmyPqkiIdHQIj3vVKXAp4PngYdpmSag1wG/8K0/YtH5OBwi57WS4PGgzkCQQCl5bG1S7EJVyb3Y13Fu4p0fL7YI3uDv5uFH4Q2IixakwWJlvU11k+LxwuWJCBZTzQvhdTypsYOboTs5OBtf6TdAkBgN1TqPBTYSZdJqdhJmV6htOyOdlqo0/8bQDlPhJFM+Xt6kWnp3ZR28G4KRMtPcioHMpefa9oju0/bh72ciCuZAkBa/U1nu1khX1yu6SAgeIkq9NSTpXp0O15oOG4CZuHe16581YAuhBni90wCkFPxyAA+ZG/0msYcWj+r0qjJRe49AkEAqezNw8a2cmuUpq2/GzgyvRn1K/FVTWLox2VZksYENtmrEsDjpvjj/2Dp8bwebkt2iLnKu4+PDGB0OOe791giUQ==";

    //????????????????????????
    @GetMapping(value = "/getTasks")
    public AjaxResponse getTasks() {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }
            Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));

            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

            for (Task tk : tasks.getContent()) {
                ProcessInstance processInstance = processRuntime.processInstance(tk.getProcessInstanceId());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", tk.getId());
                hashMap.put("name", tk.getName());
                hashMap.put("status", tk.getStatus());
                hashMap.put("createdDate", tk.getCreatedDate());
                if (tk.getAssignee() == null) {//????????????null????????????????????????
                    hashMap.put("assignee", "???????????????");
                } else {
                    hashMap.put("assignee", tk.getAssignee());//
                }

                hashMap.put("instanceName", processInstance.getName());
                listMap.add(hashMap);
            }

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);


        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "??????????????????????????????", e.toString());
        }
    }

    //??????????????????
    @GetMapping(value = "/completeTask")
    public AjaxResponse completeTask(@RequestParam("taskID") String taskID,@RequestParam("type") String type) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);


            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            if (task.getAssignee().equals("wukong")) {
                type = 2+"";
            }

            if (task.getAssignee().equals("wukong") && type=="2") {
                RequestServiceEntity requestServiceEntity = new RequestServiceEntity();
                requestServiceEntity.setServiceName("DIRECT_LOAN");
                requestServiceEntity.setPlatformNo("202107270001");
                requestServiceEntity.setKeySerial(1);
                Map<Object, Object> map = new HashMap<>();
                map.put("platformUserNo","1");
                map.put("requestNo",task.getId());
                map.put("loanType","NORMAL");
                map.put("amount",2222.00);
                map.put("projectNo","123");
                requestServiceEntity.setReqData(map);
                String s = JSONObject.toJSONString(map);
                requestServiceEntity.setSign(RSAUtils.encrypt(s.substring(0,100),publicKey));

                JSONObject date = new JSONObject();
                date.put("serviceName",requestServiceEntity.getServiceName());
                date.put("platformNo",requestServiceEntity.getPlatformNo());
                date.put("reqData",requestServiceEntity.getReqData());
                date.put("keySerial",requestServiceEntity.getKeySerial());
                date.put("sign",requestServiceEntity.getSign());

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpUtils.sendJsonStr("http://localhost:8888/bha-neo-app/lanmaotech/service",date.toString());


            }

            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    .withVariable("apply", type)//????????????????????????
                    .build());


            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "????????????", e.toString());
        }
    }

    //??????
    @GetMapping(value = "/startProcess4")
    public AjaxResponse startProcess3(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                      @RequestParam("instanceName") String instanceName,
                                      @RequestParam("instanceVariable") String instanceVariable) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("wukong");
            }


/*            @RequestMapping("/approval_msg")
            @ResponseBody
            public JsonResponse approvalPass(String id,String msg){
                JsonResponse jsonResponse = new JsonResponse();

                if(StringUtil.isNotEmpty(msg)){
                    String str= msg.replace("\"", "");
                    taskService.setVariable(id,"msg",str);
                }
                taskService.complete(id);
                return jsonResponse;
            }*/

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "??????", e.toString());
        }
    }


    //????????????
    @GetMapping(value = "/formDataShow")
    public AjaxResponse formDataShow(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);

//-----------------------????????????????????????????????????------------------------------------------------
            //????????????????????????????????????HashMap???????????????????????????????????????????????????
            HashMap<String, String> controlistMap = new HashMap<>();
            //????????????????????????????????????
            List<HashMap<String, Object>> tempControlList = mapper.selectFormData(task.getProcessInstanceId());

            for (HashMap ls : tempControlList) {
                //String Control_ID = ls.get("Control_ID_").toString();
                //String Control_VALUE = ls.get("Control_VALUE_").toString();
                controlistMap.put(ls.get("Control_ID_").toString(), ls.get("Control_VALUE_").toString());
            }
            //String controlistMapValue = controlistMap.get("??????ID");
            //controlistMap.containsKey()

            //

/*  ------------------------------------------------------------------------------
            FormProperty_0ueitp2-_!??????-_!??????-_!?????????-_!????????????
            ?????????
            FormProperty_0lovri0-_!string-_!??????-_!???????????????-_!f
            FormProperty_1iu6onu-_!int-_!??????-_!???????????????-_!s

            ?????????????????????????????????FormProperty_????????????????????????ID
            ???????????????f??????????????????s????????????t?????????(?????????int???????????????int?????????string)
            ?????????????????????????????????????????????????????????????????????????????????
            */

            //??????!!!!!!!!:??????Key????????????????????????????????????????????????????????????key??????????????????????????????????????????key???task.getFormKey()???????????????key
            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());

            if (userTask == null) {
                return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                        GlobalConfig.ResponseCode.SUCCESS.getDesc(), "?????????");
            }
            List<FormProperty> formProperties = userTask.getFormProperties();
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (FormProperty fp : formProperties) {
                String[] splitFP = fp.getId().split("-_!");

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", splitFP[0]);
                hashMap.put("controlType", splitFP[1]);
                hashMap.put("controlLable", splitFP[2]);


                //??????????????????????????????ID
                if (splitFP[3].startsWith("FormProperty_")) {
                    //??????ID??????
                    if (controlistMap.containsKey(splitFP[3])) {
                        hashMap.put("controlDefValue", controlistMap.get(splitFP[3]));
                    } else {
                        //??????ID?????????
                        hashMap.put("controlDefValue", "?????????????????????" + splitFP[0] + "??????");
                    }
                } else {
                    //?????????????????????????????????ID??????????????????
                    hashMap.put("controlDefValue", splitFP[3]);
                }


                hashMap.put("controlIsParam", splitFP[4]);
                listMap.add(hashMap);
            }

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "??????", e.toString());
        }
    }

    //????????????
    @PostMapping(value = "/formDataSave")
    public AjaxResponse formDataSave(@RequestParam("taskID") String taskID,
                                     @RequestParam("formData") String formData) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);

            //formData:??????id-_!?????????-_!????????????!_!??????id-_!?????????-_!????????????
            //FormProperty_0lovri0-_!????????????-_!f!_!FormProperty_1iu6onu-_!????????????-_!s


            HashMap<String, Object> variables = new HashMap<String, Object>();
            Boolean hasVariables = false;//??????????????????


            List<HashMap<String, Object>> listMap = new ArrayList<>();

            //????????????????????????????????????????????????
            String[] formDataList = formData.split("!_!");//
            for (String controlItem : formDataList) {
                String[] formDataItem = controlItem.split("-_!");

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("PROC_DEF_ID_", task.getProcessDefinitionId());
                hashMap.put("PROC_INST_ID_", task.getProcessInstanceId());
                hashMap.put("FORM_KEY_", task.getFormKey());
                hashMap.put("Control_ID_", formDataItem[0]);
                hashMap.put("Control_VALUE_", formDataItem[1]);
                listMap.add(hashMap);

                //??????????????????
                switch (formDataItem[2]) {
                    case "f":
                        System.out.println("????????????????????????");
                        break;
                    case "s":
                        variables.put(formDataItem[0], formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        variables.put(formDataItem[0], timeFormat.parse(formDataItem[2]));
                        hasVariables = true;
                        break;
                    case "b":
                        variables.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[2]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("?????????????????????????????????" + formDataItem[0] + "???????????????????????????" + formDataItem[2]);
                }
            }//for??????

            if (hasVariables) {
                //?????????????????????
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                        .withVariables(variables)
                        .build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskID)
                        .build());
            }

            //???????????????
            int result = mapper.insertFormData(listMap);

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "??????", e.toString());
        }
    }

}
