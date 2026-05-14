# 贡献指南

感谢你对 JMeter Dubbo Plugin 项目的关注！我们欢迎各种形式的贡献。

## 📋 目录

- [行为准则](#行为准则)
- [如何贡献](#如何贡献)
- [开发环境设置](#开发环境设置)
- [提交规范](#提交规范)
- [Pull Request 流程](#pull-request-流程)

## 行为准则

本项目采用 [Contributor Covenant](CODE_OF_CONDUCT.md) 行为准则。参与即表示你同意遵守该准则。

## 如何贡献

### 报告 Bug

1. 在 [Issues](https://github.com/solsynx/jmeter-dubbo-plugin/issues) 中搜索是否已有类似问题
2. 如果没有，创建新的 Issue
3. 提供详细信息：
   - JMeter 版本
   - Dubbo 版本
   - **注册中心类型及版本**（如：ZooKeeper 3.4.14、Nacos 2.x 等）
   - 操作系统
   - 复现步骤
   - 错误日志

### 提出新功能

1. 先在 Issues 中讨论新功能的可行性
2. 说明使用场景和预期效果

### 代码贡献

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: add AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 开发环境设置

### 前置要求

- Java 8 或更高版本
- Maven 3.6+
- Apache JMeter 5.6.3

### 构建项目

```bash
# 克隆仓库
git clone https://github.com/solsynx/jmeter-dubbo-plugin.git
cd jmeter-dubbo-plugin

# 构建项目
./mvnw clean install

# 运行测试
./mvnw test
```

### IDE 配置

推荐使用 IntelliJ IDEA 或 Eclipse 导入 Maven 项目。

## 提交规范

我们遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type 类型**:
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具变动

**示例**:
```
feat: add custom Dubbo serialization

- Implement DubboSerialization class
- Register via SPI mechanism

Closes #123
```

## Pull Request 流程

### 提交前检查清单

- [ ] 代码符合项目规范
- [ ] 添加了必要的测试
- [ ] 所有测试通过
- [ ] 更新了相关文档
- [ ] Commit message 符合规范

### PR 审查流程

1. 自动检查（CI/CD）必须通过
2. 至少一位维护者审查代码
3. 解决所有审查意见
4. 合并到主分支

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 使用 SLF4J 记录日志
- 所有公共方法添加 JavaDoc
- 保持代码简洁清晰

## 测试要求

- 单元测试覆盖率 ≥ 70%
- 新增功能必须包含测试用例
- 确保不破坏现有功能

## 文档更新

如果修改影响了用户使用，请同步更新：
- README.md
- 相关 Wiki 页面
- 示例代码

## 获取帮助

- 📧 邮件: xy.0520@hotmail.com
- 💬 [Discussions](https://github.com/solsynx/jmeter-dubbo-plugin/discussions)
- 🐛 [Issues](https://github.com/solsynx/jmeter-dubbo-plugin/issues)

## 致谢

感谢所有贡献者！🎉
