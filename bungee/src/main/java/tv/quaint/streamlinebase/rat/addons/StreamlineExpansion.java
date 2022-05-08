package tv.quaint.streamlinebase.rat.addons;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.rat.RATExpansion;
import tv.quaint.streamlinebase.savables.users.SavableUser;
import tv.quaint.streamlinebase.self.pages.JoinPage;
import tv.quaint.streamlinebase.utils.BasePlayerHandler;
import tv.quaint.streamlinebase.utils.SavableHandler;

import java.util.List;

public class StreamlineExpansion extends RATExpansion {
    public StreamlineExpansion() {
        super("streamline", "Quaint", "0.0.0.1");
    }

    @Override
    public String onLogic(String params) {
        if (params.equals("prefix")) return StreamlineBase.MESSAGES.prefix;
        if (params.equals("version")) return StreamlineBase.getVersion();
        if (params.equals("players_online")) return String.valueOf(BasePlayerHandler.getOnlinePlayers().size());
        if (params.equals("players_loaded")) return String.valueOf(SavableHandler.getJustPlayers().size());
        if (params.equals("staff_online")) return String.valueOf(BasePlayerHandler.getOnlineStaff().size());

        if (params.matches("([a][u][t][h][o][r][\\[]([0])[\\]])")) {
            Pattern pattern = Pattern.compile("([a][u][t][h][o][r][\\[]([0])[\\]])");
            Matcher matcher = pattern.matcher(params);
            while (matcher.find()) {
                return StreamlineBase.INSTANCE.getDescription().getAuthor();
            }
        }

        if (params.matches("([s][t][a][f][f][\\[]([0-" + (BasePlayerHandler.getOnlineStaffNames().size() - 1) + "])[\\]])")) {
            Pattern pattern = Pattern.compile("([s][t][a][f][f][\\[]([0-" + (BasePlayerHandler.getOnlineStaffNames().size() - 1) + "])[\\]])");
            Matcher matcher = pattern.matcher(params);
            while (matcher.find()) {
                int i = Integer.parseInt(matcher.group(2));
                if (i >= BasePlayerHandler.getOnlineStaffNames().size()) i = BasePlayerHandler.getOnlineStaffNames().size() - 1;

                return BasePlayerHandler.getOnlineStaffNames().get(i);
            }
        }

        return null;
    }

    @Override
    public String onRequest(SavableUser user, String params) {
        if (params.equals("join_first")) {
            return new JoinPage(user.uuid, false).get().getField("first").value.thing.toString();
        }
        if (params.equals("join_latest")) {
            return new JoinPage(user.uuid, false).get().getField("latest").value.thing.toString();
        }
        return null;
    }
}
