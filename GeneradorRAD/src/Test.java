import java.util.regex.*;

public class Test
{
	public static String transform(String input) {
        // 1. Verifica si es una asignación del tipo ((Agent) agentX).field = ...
        Pattern assignPattern = Pattern.compile("^\\s*\\(\\(Agent\\)\\s*(agent\\w+)\\)\\.(\\w+)\\s*=\\s*(.+);?$");
        Matcher assignMatcher = assignPattern.matcher(input);

        if (assignMatcher.find()) {
            String lhsAgent = assignMatcher.group(1);  // Ej: agenti
            String lhsField = assignMatcher.group(2);  // Ej: rho
            String rhs = assignMatcher.group(3);       // Expresión derecha

            String transformedRhs = transformAgentFields(rhs, lhsAgent);
            String msgVar = lhsAgent.replaceFirst("agent", "msg");

            return msgVar + ".set(\"" + lhsField + "\", " + transformedRhs + ");";
        } else {
            // Si no es asignación de ((Agent)...), solo reemplazar ocurrencias del patrón
            return transformAgentFields(input, null);
        }
    }

    private static String transformAgentFields(String input, String lhsAgent) {
        Pattern fieldPattern = Pattern.compile("\\(\\(Agent\\)\\s*(agent\\w+)\\)\\.(\\w+)");
        Matcher matcher = fieldPattern.matcher(input);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String agent = matcher.group(1);  // Ej: agenti, agentj
            String field = matcher.group(2);  // Ej: rt, conf, repu

            String replacement;
            if (lhsAgent != null && agent.equals(lhsAgent)) {
                replacement = "msgi.get(\"" + field + "\")";
            } else {
                String msgVar = agent.replaceFirst("agent", "msg");
                replacement = msgVar + ".get(\"" + field + "\")";
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
	public static void main(String[] args) {
		String example1 = "((Agent)agenti).rho = ((Agent)agenti).conf + ((Agent)agentj).repu + 0.5";
        String example2 = "model.fs = ((1 + ((Agent) agenti).rt * ((Agent) agentj).rt)) * 0.5";

        System.out.println(transform(example1));
        System.out.println(transform(example2));
    }

}
