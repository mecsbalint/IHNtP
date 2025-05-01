import { Outlet } from "react-router-dom";
import CustomNavbar from "../../components/Navbar/CustomNavbar";

function Layout() {

    return (
        <>
        <CustomNavbar />
        <Outlet />
        </>
    )
}

export default Layout;
