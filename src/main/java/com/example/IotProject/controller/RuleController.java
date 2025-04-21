package com.example.IotProject.controller;

import com.example.IotProject.response.RuleResponse.RuleAndConditionResponse;
import com.example.IotProject.response.StringResponse;
import com.example.IotProject.service.RuleService.ConditionRuleService;
import com.example.IotProject.service.RuleService.IConditionRuleService;
import com.example.IotProject.service.RuleService.IRuleService;
import org.springframework.web.bind.annotation.*;

import com.example.IotProject.dto.RuleDTO;
import com.example.IotProject.model.RuleModel;
import com.example.IotProject.response.RuleResponse.RuleResponse;
import com.example.IotProject.service.RuleService.RuleService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/v1/rules")
public class RuleController {
    private final IConditionRuleService conditionRuleService;
    private final IRuleService ruleService;
    public RuleController(ConditionRuleService conditionRuleService, RuleService ruleService) {
        this.conditionRuleService = conditionRuleService;
        this.ruleService = ruleService;
    }
    @PostMapping("/create")
    public ResponseEntity<StringResponse> createRule(@RequestBody RuleDTO ruleDTO) {
        ruleService.createRule(ruleDTO);
        return ResponseEntity.ok(new StringResponse("Rule created successfully"));
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<RuleResponse>> getAllRules() {
        List<RuleModel> rules = ruleService.getAllRules();
        List<RuleResponse> ruleResponses = rules.stream()
                .map(rule -> new RuleResponse(rule.getId(), rule.getAction(), rule.getDevice().getFeedName(), rule.getUser().getId()))
                .toList();
        return ResponseEntity.ok(ruleResponses);
    }
    @PutMapping("/update")
    public ResponseEntity<StringResponse> updateRule(@RequestParam Long id, @RequestBody RuleDTO ruleDTO) {
        ruleService.updateRule(id, ruleDTO);
        return ResponseEntity.ok(new StringResponse("Rule updated successfully"));
    }
    @GetMapping("/getRuleById")
    public ResponseEntity<RuleResponse> getRuleById(@RequestParam Long id) {
        RuleModel rule = ruleService.getRuleById(id);
        if (rule != null) {
            RuleResponse ruleResponse = new RuleResponse(rule.getId(), rule.getAction(), rule.getDevice().getFeedName(), rule.getUser().getId());
            return ResponseEntity.ok(ruleResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @DeleteMapping("/delete")
    public ResponseEntity<StringResponse> deleteRule(@RequestParam Long id) {
        ruleService.deleteRule(id);
        return ResponseEntity.ok(new StringResponse("Rule deleted successfully"));
    }

    @GetMapping("/getRulesByDevice")
    public ResponseEntity<List<RuleAndConditionResponse>> getRulesByDevice(@RequestParam String feedName) {
        List<RuleModel> rules = ruleService.getRulesbyDevice(feedName);
        if (rules != null && !rules.isEmpty()) {
            List<RuleAndConditionResponse> ruleResponses = rules.stream()
                    .map(rule -> new RuleAndConditionResponse(
                            rule.getId(),
                            rule.getAction(),
                            rule.getDevice().getFeedName(),
                            rule.getUser().getId(),
                            conditionRuleService.getConditionByRule(rule.getId())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ruleResponses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getRulesAndConditionRulesByAction/{actuator_name}/{zone_id}")
    public ResponseEntity<List<RuleAndConditionResponse>> getRulesAndConditionRulesByAction(
            @PathVariable("actuator_name") String actuatorName,
            @PathVariable("zone_id") Long zoneId) {
        List<RuleModel> rules = ruleService.getRuleByActuatorNameAndZoneId(actuatorName, zoneId);
        if (rules != null && !rules.isEmpty()) {
            List<RuleAndConditionResponse> ruleResponses = rules.stream()
                    .map(rule -> new RuleAndConditionResponse(
                            rule.getId(),
                            rule.getAction(),
                            rule.getDevice().getFeedName(),
                            rule.getUser().getId(),
                            conditionRuleService.getConditionByRule(rule.getId())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ruleResponses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
