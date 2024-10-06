package com.example.myapplication.Const

import com.example.myapplication.R

object ModelList {
    val items = listOf(
        "GPT-4o",
        "Kimi",
        "GPT-4o-mini",
        "Claude-3.5-sonnet",
        "Claude-3-haiku",
        "GPT-4o-all",
        "AskInternet",
    )

    val modelStore = listOf(
        "GPT-4o",
        "GPT-4o-mini",
        "GPT-4o-all",
        "GPT-4-turbo",
        "GPT-3.5-turbo",
        "GPT-4-all",
        "GPT-4-vision",
        "GPT-4-origin",
        "Claude-3.5-sonnet",
        "Claude-3-haiku",
        "Claude-3-opus",
        "DeepSeek",
        "DeepSeek-coder",
        "Doubao-pro",
        "Kimi",
        "AskInternet",
        "Llama3-70b",
        "Gemini",
        "Codellama"
    )

    val modelStoreMap = mapOf(
        "GPT-4o" to "gpt-4o",
        "GPT-4o-mini" to "gpt-4o-mini",
        "GPT-4o-all" to "gpt-4o-all",
        "GPT-4-turbo" to "gpt-4-turbo-preview",
        "GPT-3.5-turbo" to "gpt-3.5-turbo",
        "GPT-4-all" to "gpt-4-all",
        "GPT-4-vision" to "gpt-4-vision-preview",
        "GPT-4-origin" to "gpt-4-origin",
        "Claude-3.5-sonnet" to "claude-3.5-sonnet",
        "Claude-3-haiku" to "claude-3-haiku",
        "Claude-3-opus" to "claude-3-opus",
        "Doubao-pro" to "doubao-pro-32k",
        "DeepSeek-coder" to "deepseek-coder",
        "DeepSeek" to "deepseek-chat",
        "Kimi" to "kimi",
        "AskInternet" to "gpt-ask-internet",
        "Llama3-70b" to "llama3-70b",
        "Gemini" to "gemini",
        "Codellama" to "codellama"
    )

    val describeMap = mapOf(
        "GPT-4o" to "OpenAI最新发布的强大模型。在定量问题（数学和物理）、创意写作以及许多其他挑战性任务中，表现比ChatGPT更强。速度更快。强烈建议首选。",
        "GPT-4o-mini" to "OpenAI最新发布的最强小模型。旨在替代 GPT-3.5-Turbo 模型，更快更强。",
        "GPT-4o-all" to "支持图片生成，联网查询等多模功能的GPT-4o，需要生成图片时或代码执行选择。" +
                "此模型为逆向官网模型，所以稳定性相对较差，请优先使用 GPT-4o 模型。",
        "GPT-4-turbo" to "OpenAI的最强大模型。在定量问题（数学和物理）、创意写作以及许多其他挑战性任务中，表现比ChatGPT更强。",
        "GPT-3.5-turbo" to "OpenAI 最快速的模型，价格便宜，速度快，能胜任日常大部分任务，建议优先使用。",
        "GPT-4-all" to "支持图片生成，联网查询等多模功能的GPT-4，速度较慢，需要生成图片时选择。" +
                "此模型为逆向官网模型，所以稳定性相对较差，请优先使用 GPT-4 模型。",
        "GPT-4-vision" to "专门用于图像识别领域的模型",
        "GPT-4-origin" to "最基础的GPT-4模型",
        "Claude-3.5-sonnet" to "Anthropic 最强的 AI 模型 Claude 3.5 Sonnet，大多数基准测试结果中更是优于 GPT-4o。",
        "Claude-3-haiku" to "Anthropic速度最快的模型，能够处理复杂分析、多步骤的长任务以及高阶数学和编码任务。比GPT-3.5 速度更快，更智能。",
        "Claude-3-opus" to "Anthropic最智能的模型，能够处理复杂分析、多步骤的长任务以及高阶数学和编码任务。",
        "DeepSeek" to "DeepSeek-V2 基于 2 千亿 MoE 模型底座，领先性能，超低价格，越级场景体验。擅长通用对话任务。",
        "DeepSeek-coder" to "DeepSeek-V2 基于 2 千亿 MoE 模型底座，领先性能，超低价格，越级场景体验。擅长通用对话任务。",
        "Doubao-pro" to "字节跳动推出的自研大模型。通过字节内部50+业务场景实践验证，每日千亿级tokens大使用量持续打磨，提供多模态能力，以优质模型效果为企业打造丰富的业务体验。",
        "Kimi" to "尚未上线的GPT-4.5 Turbo上下文窗口指定为25.6万个token，能同时处理约20万个单词，Kimi升级后，长文本能力为其10倍。在2023年10月初次亮相时，该模型的处理能力还仅有约20万汉字。",
        "AskInternet" to "基于 FreeAskInternet 和 searxng 专门联网搜索模型，代替 perplexity.ai，目前只支持中文。适合联网搜索，并整理搜索结果，主要用于替代传统搜索引擎。\n" +
                "请输入关键词来进行搜索，不需要输入完整的提示词。\n" +
                "\n" +
                "注意：该模型适合单次的搜索问答对话，不记忆上下文。",
        "Llama3-70b" to "Meta发布了开源大模型Llama-3，是一款性能强大的大型语言模型。Llama-3有700亿参数模型。相比Llama-2，Llama-3在推理、数学、代码生成、指令跟踪等方面有了显著提升。它采用了分组查询注意力、掩码等创新技术，有助于开发者以更低的能耗获得更好的性能。Llama-3还使用了128K的词汇表标记器，提高了处理语言的灵活性。预训练数据集包含超过15T tokens，比Llama-2的数据集大了7倍，其中包含超过5%的高质量非英语数据，涵盖30多种语言。为了测试其性能，Meta还开发了一个包含1800个提示的高质量人类评估数据集，覆盖12个关键用例。测试结果显示，Llama-3的性能明显超过了一些同类模型，包括Claude Sonnet、Mistral Medium和GPT-3.5。同时，Meta已经将Llama-3应用于其旗下产品，包括Instagram、WhatsApp和Facebook，并提供了图像生成器等功能。此外，Llama-3的基础模型已经可通过Amazon SageMaker JumpStart进行部署和推理运行。",
        "Gemini" to "谷歌最新推出的Gemini 1.5 Pro实验版本（0827）在多项评测中表现出色，特别是在LMSYS Chatbot Arena排行榜上以1300的ELO分数高居榜首，超越了OpenAI的GPT-4o（ELO：1286）和Anthropic的Claude-3.5 Sonnet（ELO：1271）。这一成绩不仅证明了Gemini 1.5 Pro在多语言任务、数学、Hard Prompt和编码等领域的卓越性能，更在视觉能力上与GPT-4o和Claude 3.5 Sonnet不相上下。",
        "Codellama" to "Code Llama 是Meta推出的代码生成大模型。擅长编程和代码处理。"
    )

    val modelPictureMap = mapOf(
        "GPT-4o" to R.drawable.gpt4,
        "GPT-4o-mini" to R.drawable.gpt4,
        "GPT-4o-all" to R.drawable.gpt432k,
        "GPT-4-turbo" to R.drawable.gpt432k,
        "GPT-3.5-turbo" to R.drawable.gpt35turbo,
        "GPT-4-all" to R.drawable.gpt432k,
        "GPT-4-vision" to R.drawable.gpt4_vision,
        "GPT-4-origin" to R.drawable.gpt4,
        "Claude-3.5-sonnet" to R.drawable.claude,
        "Claude-3-haiku" to R.drawable.claude,
        "Claude-3-opus" to R.drawable.claude100k,
        "Doubao-pro" to R.drawable.doubao,
        "DeepSeek-coder" to R.drawable.deepseek,
        "DeepSeek" to R.drawable.deepseek,
        "Kimi" to R.drawable.kimi,
        "AskInternet" to R.drawable.ask_internet,
        "Llama3-70b" to R.drawable.llama,
        "Gemini" to R.drawable.gemini,
        "Codellama" to R.drawable.llama
    )
}