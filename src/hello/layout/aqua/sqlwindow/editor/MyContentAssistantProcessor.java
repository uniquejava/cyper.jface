package hello.layout.aqua.sqlwindow.editor;

import hello.cache.Field;
import hello.cache.TableCache;
import hello.filter.FmsRule;
import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.SqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.kitten.core.util.StringUtil;

public class MyContentAssistantProcessor implements IContentAssistProcessor {

	// 获得内容的提示数组
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		System.out.println("computeCompletionProposals");
		final int cursor = offset;

		// (1)取得用户已经输入的字符
		IDocument doc = viewer.getDocument();
		// offset为当前光标所在的位置
		StringBuffer sb = new StringBuffer();
		
		
//		boolean endsWithDot = false;
//		try {
//			char lastChar = doc.getChar(offset-1);
//			if (lastChar == '.') {
//				endsWithDot = true;
//				offset--;//用户输入的最后一个点不计算在内.
//			}
//		} catch (BadLocationException e1) {
//			e1.printStackTrace();
//		}
		
		// 从当前位置向后查找，直到遇到空格为止，然后将字符串反转
		String userEntered = getUserEntered(offset, doc, sb);
		
		System.out.println("userEntered="+userEntered);
		
		String schema = CyperDataStudio.getStudio().getTableFilter().getRule().getSchemaPattern();
		if (StringUtil.isNotBlank(schema)) {
			if (userEntered.toUpperCase().startsWith(schema)) {
				userEntered = userEntered.substring(schema.length()+1);
			}
		}
		System.out.println("userEntered="+userEntered);
		
		boolean methoding = userEntered.indexOf(".")!=-1;
		
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		if (methoding) {
			String wholeSqlBlock = SqlUtil.getWholeSqlBlock(viewer.getTextWidget());
			Map<String,String> aliasMapping = SqlUtil.getAliasMapping(wholeSqlBlock);
			
			list = handleMethodingProposal(cursor, userEntered,aliasMapping);
		}else{
			list = handleKeywordProposal(cursor, userEntered);
		}
		
		return list.toArray(new CompletionProposal[list.size()]);
	}

	private String getUserEntered(int offset, IDocument doc, StringBuffer sb) {
		while (true) {
			try {
				// 获得前一个字符
				char c = doc.getChar(--offset);
				if (Character.isWhitespace(c)) {
					break;
				} /*else if (c == '.') {
					break;
				}*/
				sb.append(c);
			} catch (BadLocationException e) {
				break;
			}
		}
		String userEntered = sb.reverse().toString();
		return userEntered;
	}

	private List<CompletionProposal> handleMethodingProposal(final int cursor, String userEntered, Map<String, String> aliasMapping) {
		//点号前的内容
		String objectPart = userEntered.substring(0,userEntered.indexOf("."));
		
		//是否有别名映射，如果是，根据别名找到真实表名.
		if (aliasMapping.get(objectPart.toUpperCase())!=null) {
			objectPart = aliasMapping.get(objectPart.toUpperCase());
		}
		
		//点号后的内容，可以为空白
		String methodPart = userEntered.substring(userEntered.indexOf(".")+1);
		
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		
		
		//Map<String,List<String>> tips = ResourceManager.getTips();
//		if(tips.keySet().contains(objectPart)){
		final TableCache cache = TableCache.getInstance();
		if(cache.getTableColumnInfo(objectPart)!=null){
//			List<String> methods = tips.get(objectPart);
			Set<Field> methods = cache.getTableColumnInfo(objectPart).getFields();
			
			for(Field method: methods){
				if (method.getName().toLowerCase().startsWith(methodPart.toLowerCase())) {
					String replacementString = method.getName();
					int replacementOffset = cursor - methodPart.length();
					int replacementLength = methodPart.length();
					int newCursorPosition = method.getName().length()/*-1*/;//method的话有参数就放到括号里
					String displayString = method.getName();
					Image image = ImageFactory.loadImage(ImageFactory.METHOD);
					CompletionProposal proposal = new CompletionProposal(
							replacementString, replacementOffset,
							replacementLength, newCursorPosition,image,displayString,null,null);
					list.add(proposal);
				}
			}
		}
		
		return list;
	}
	
	private List<CompletionProposal> handleKeywordProposal(final int cursor, String userEntered) {
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		final TableCache cache = TableCache.getInstance();
		String[] assistant_keywords = cache.getContentAssistantKeywords();
		for (int i = 0; i < assistant_keywords.length; i++) {
			if (assistant_keywords[i].toLowerCase().startsWith(userEntered.toLowerCase())) {
				String replacementString = assistant_keywords[i];
				int replacementOffset = cursor - userEntered.length();
				int replacementLength = userEntered.length();
				int newCursorPosition = replacementString.length();//放到最后的位置
				String displayString = assistant_keywords[i];
				CompletionProposal proposal = new CompletionProposal(
						replacementString, replacementOffset,
						replacementLength, newCursorPosition,null,displayString,null,null);
				list.add(proposal);
			}
		}
		return list;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[]{'.'};
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
