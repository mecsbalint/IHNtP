import { Outlet } from "react-router-dom";
import CustomNavBar from "../../components/CustomNavBar/CustomNavBar";

function Layout() {

    return (
        <div className="bg-cover grid grid-cols-1 overflow-hidden place-items-stretch bg-white gap-2">
            <div>
                <CustomNavBar />
            </div>
            <div className="flex justify-center bg-blue-400 rounded-box">
                <div className="w-200">
                    <Outlet />
                </div>
            </div>
        </div>
    );
}

export default Layout;
