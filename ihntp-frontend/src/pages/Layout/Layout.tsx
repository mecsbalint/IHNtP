import { Outlet } from "react-router-dom";
import CustomNavBar from "../../components/CustomNavBar/CustomNavBar";

function Layout() {

    return (
        <div className="bg-cover grid grid-cols-4 overflow-hidden place-items-stretch">
            <div className="col-span-4">
                <CustomNavBar />
            </div>
            <div className="col-start-2 col-span-2">
                <Outlet />
            </div>
        </div>
    );
}

export default Layout;
