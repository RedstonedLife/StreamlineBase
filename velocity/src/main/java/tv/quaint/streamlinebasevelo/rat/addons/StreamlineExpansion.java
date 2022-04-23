package tv.quaint.streamlinebasevelo.rat.addons;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.rat.RATExpansion;
import tv.quaint.streamlinebasevelo.savables.users.SavableUser;
import tv.quaint.streamlinebasevelo.self.pages.JoinPage;
import tv.quaint.streamlinebasevelo.utils.BasePlayerHandler;

public class StreamlineExpansion extends RATExpansion {
    public StreamlineExpansion() {
        super("streamline", "Quaint", "0.0.0.1");
    }

    @Override
    public String onLogic(String params) {
        if (params.equals("prefix")) return StreamlineBase.MESSAGES.prefix;
        if (params.equals("version")) return StreamlineBase.getVersion();
        if (params.equals("players_online")) return String.valueOf(BasePlayerHandler.getOnlinePlayers().size());
//        if (params.equals("players_loaded")) return String.valueOf(BasePlayerHandler.getStats().size());
//        if (params.equals("staff_online")) return String.valueOf(BasePlayerHandler.getJustStaffOnline().size());

        if (params.matches("([a][u][t][h][o][r][\\[]([0-2])[\\]])")) {
            Pattern pattern = Pattern.compile("([a][u][t][h][o][r][\\[]([0-9])[\\]])");
            Matcher matcher = pattern.matcher(params);
            while (matcher.find()) {
                return StreamlineBase.getDescription().getAuthors().get(Integer.parseInt(matcher.group(2)));
            }
        }

//        if (params.matches("([s][t][a][f][f][\\[]([0-" + (BasePlayerHandler.getNamesJustStaffOnline().size() - 1) + "])[\\]])")) {
//            Pattern pattern = Pattern.compile("([s][t][a][f][f][\\[]([0-" + (BasePlayerHandler.getNamesJustStaffOnline().size() - 1) + "])[\\]])");
//            Matcher matcher = pattern.matcher(params);
//            while (matcher.find()) {
//                return BasePlayerHandler.getNamesJustStaffOnline().get(Integer.parseInt(matcher.group(2)));
//            }
//        }

        return null;
    }

    @Override
    public String onRequest(SavableUser user, String params) {
        if (params.equals("join_first")) {
            return new JoinPage(user.uuid).getField("first").property.toString();
        }
        if (params.equals("join_latest")) {
            return new JoinPage(user.uuid).getField("latest").property.toString();
        }
        return null;
    }
}
