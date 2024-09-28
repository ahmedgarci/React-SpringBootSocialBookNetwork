import { SingleCard } from "./Card";

function AllBooks(){
    return (
        <div className="mt-5 grid grid-cols-2 gap-0">
        <SingleCard/>
        <SingleCard/>

        </div>
    )
}
export {AllBooks}