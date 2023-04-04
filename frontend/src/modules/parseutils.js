export class ParseUtils {

    getSelectedExistingOrg(user, userOrgsRspOrgs) {
        const selectedOrg = user.getSelectedOrg()
        if (selectedOrg === null) {
            return null
        }
        for (let i = 0; i < userOrgsRspOrgs.length; i++) {
            if (userOrgsRspOrgs[i].id == selectedOrg) {

            }
        }
    }

}