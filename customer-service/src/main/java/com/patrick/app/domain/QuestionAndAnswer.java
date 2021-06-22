package com.patrick.app.domain;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Patrick Pan
 *
 */
@Slf4j
@Getter
public enum QuestionAndAnswer {

	_1st("如何办理开户手续",
			"需客户本人在交易时间内携带有效身份证明文件、银行卡或存折、股东卡（若无则无\n" + "需携带）到我司营业部办理开户手续。您也可使用微信的“我要开户”功能，输入好\n"
					+ "您的信息，我司有专人和您联系开户事宜。"),
	_2nd("开户所需身份证明文件包括什么", "（一）境内自然人：有效身份证明文件为中华人民共和国居民身份证。\n" + "（二）境内法人：有效身份证明文件为工商营业执照、社团法人注册登记证书、机关\n"
			+ "事业法人成立批文等。\n" + "（三）境内合伙及创投企业等非法人组织：有效身份证明文件为工商管理部门颁发的\n" + "营业执照或国家有权机关颁发的合伙组织成立证书。\n"
			+ "（四）境外自然人：有效身份证明文件是指境外所在国家或地区的护照或居民身份\n" + "证；有境外国家或地区永久居留权的中国公民的永久居留证明及中国护照；公安机关\n"
			+ "发放的外国人永久居留证；香港、澳门特区居民身份证和港澳居民来往内地通行证\n" + "（其前身是俗称“回乡证”的《港澳同胞回乡证》）；台湾居民来往大陆通行证（简\n" + "称“台胞证”）等。\n"
			+ "（五）境外法人：有效身份证明文件是指境外所在国家或地区有效商业登记证明文件\n" + "或其他与商业登记证明文件具有同等法律效力的可证明机构开立的文件，如机构投资\n"
			+ "者在当地税务机关缴纳税款的登记税号、其他与该机构成立有关的文件编号等。\n" + "（六）在境内工作生活的港、澳、台居民，有效身份证明文件是指：1、港澳居民来\n"
			+ "往内地通行证，或台湾居民来往大陆通行证。2、香港、澳门、台湾居民身份证。3、\n" + "境内公安机关出具的《境外人员临时住宿登记表》。【须同时具备以上三种证件】\n"
			+ "（七）获得境内永久居留资格的境外居民：有效身份证明文件是指中华人民共和国外\n" + "国人永久居留证。"),
	_3rd("新开立的证券帐户何时可以买入股票？", "上海A股证券帐户和上海B股证券帐户均在开户后第二个交易日方能买入股票。\n" + "深圳A股证券帐户和深圳B股证券帐户于开户当日即可买入股票。"),
	_4th("如何开通创业板", "个人客户若股票交易经验已满2年，可携带身份证、股东卡到开户营业部办理创业板\n" + "权限开通手续；机构客户无需办理开通手续。"),
	_5th("开通创业板权限后何时能交易创业板股票", "具有两年以上（含两年）股票交易经验的自然人客户两个交易日后可开通创业板交易\n" + "权限（即T日签署书面风险揭示书，T+2交易日当天可交易创业板。"),
	_6th("创业板市场介绍",
			"创业板市场是为了适应自主创新企业及其他成长型创业企业发展需要而设立的市场。\n" + "在创业板市场上市的公司大多从事高科技业务，具有较高的成长性。创业板市场最大\n"
					+ "的特点是低门槛进入，严要求运作。它的成长性和市场风险均要高于主板，适合于具\n" + "有成熟的投资理念，有较强的风险承受能力和市场分析能力的投资者。"),
	_7th("创业板股票交易费用", "创业板的交易佣金、经手费率等，参照在深交所上市交易的主板A股标准执行。"),
	_8th("开放式基金账户开立",
			"一、未在我司营业部开立或登记过任何开放式基金账户\n" + "客户本人持有效身份证件在交易时间到营业部柜台开立任意基金公司的开放式基金账\n" + "户，或登记已在别处开立的开放式基金账户。\n"
					+ "二、已在我司营业部开立或登记了任意一家或一家以上基金公司的开放式基金账户\n" + "可在交易时间通过以下方式开立或登记其他开放式基金账户：\n"
					+ "1、通过网上交易或手机至强版、手机至慧版的“基金开户”功能自助办理。\n" + "2、通过拨打客服热线95575转人工办理。\n" + "3、到该营业部柜台办理。"),
	_9th("如何申购、赎回开放式基金", "您可通过我司网上交易、手机证券、电话委托等系统进行申购、赎回，若有疑问可拨\n" + "打我司全国统一客服热线95575转9人工咨询。"),
	_10th("赎回基金后资金何时到账", "如果赎回成功，资金一般T+3-5个工作日到账，其中：\n" + "货币基金：一般资金T+2到账；\n" + "QDII基金：资金一般T+13个工作日内到账，具体以基金公告为准；"),
	_11th("基金申购费用优惠", "通过我司网上交易、手机证券申购部分开放式基金有申购费率优惠，详情请拨打我司\n" + "全国统一客服热线95575转9人工咨询。"),
	_12th("LOF基金",
			"LOF基金即上市开放式基金，为深圳市场品种，即可同时在场内、场外进行基金份额\n" + "申购、赎回（按净值成交），又可在交易所场内进行基金份额买卖（按行情价格成\n"
					+ "交），并通过份额转托管机制将场外市场与场内市场有机地联系在一起。\n" + "基金代码一般是：16XXXX。\n"
					+ "场内买卖、场内申赎使用深圳A股股东账户或深圳证券投资基金账户；场外申赎使用\n" + "中登深市TA账户。"),
	_13th("分级基金",
			"分级基金是指在一只基金内部通过结构化的设计或安排，将普通基金份额拆分为具有\n" + "不同预期收益与风险的两类(级)或多类(级)份额并可分离上市交易的一种基金产品。\n"
					+ "一般分为两类份额，一类是预期风险和收益均较低且优先享受收益分配的部分，称为\n" + "低风险收益端（优先份额）；一类是预期风险和收益均较高且次优先享受收益的部\n"
					+ "分，称为高风险收益端（进取份额）。\n" + "国内现有的分级基金分为封闭式和开放式两种运作方式。一般代码设置：母基金\n" + "16XXXX，子基金：15XXXX。\n"
					+ "封闭式分级基金是将基金份额分成两类风险收益不同的基金份额，同时在封闭期内所\n" + "分成的两类份额或其中一类分别上市交易，但不能申购、赎回。\n"
					+ "开放式分级基金可分为场内与场外两部分。场外仅存在基础份额，与普通开放式基金\n" + "无异；场内基础份额可分级成收益风险不同的两类份额，因此场内存在三类份额，即\n"
					+ "基础份额、A类份额和B类份额。在基金合同成立后，所有基础份额可进行日常申购、\n" + "赎回；而分级成的两类份额可分别在交易所上市交易。另外，场内的基础份额可以分\n"
					+ "拆成A类和B类份额，A类和B类份额也可以合并成基础份额。"),
	_14th("融资融券开户条件", "最关键的条件如下：\n" + "1、申请人已在我司开立实名普通证券交易账户，且在我司从事证券交易的时间连续\n" + "计算超过半年；\n"
			+ "2、申请当日的证券资产原则上不低于人民币20万元；\n" + "3、公司规定的其他条件。");

	private String question;
	private String answer;
	@Setter
	@Getter(AccessLevel.NONE)
	private List<String> words;
	@Getter(AccessLevel.NONE)
	private AtomicInteger count; // the number of being selected by users

	private QuestionAndAnswer(String question, String answer) {
		this.question = question;
		this.answer = answer;
		count = new AtomicInteger();
	}

	public void increaseCount() {
		log.info("=== The question \"{}\" is selected. ===", question);
		count.incrementAndGet();
		log.debug("=== The question \"{}\" is totally selected for {} times. ===", question, count.get());
	}

	public int getCount() {
		return count.get();
	}

	public Correlation computeCorrelation(List<String> targetWords) {
		return new Correlation(targetWords);
	}

	/**
	 * 
	 * This class is used for sort.
	 * 
	 * @author Patrick Pan
	 *
	 */
	public class Correlation implements Comparable<Correlation> {
		@Getter
		private int hits; // the number of words which belongs to a user input

		private Correlation(List<String> targetWords) {
			for (String targetWord : targetWords) {
				if (words.contains(targetWord)) {
					hits++;
				}
			}
		}

		public String getQuestion() {
			return question;
		}

		public int getCount() {
			return count.get();
		}

		@Override
		public int compareTo(Correlation o) {
			if (hits > o.hits) {
				return -1;
			} else if (hits < o.hits) {
				return 1;
			}

			int c1 = count.get();
			int c2 = o.getCount();
			if (c1 > c2) {
				return -1;
			} else if (c1 < c2) {
				return 1;
			} else {
				return question.compareTo(o.getQuestion());
			}
		}
	}
}
