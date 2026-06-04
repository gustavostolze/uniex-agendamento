import './App.css'
import { getServicesByProfessional } from './services/serviceApi'

function App() {

  function getServices() {
    console.log(getServicesByProfessional(1));
  }

  return (
    <>
      <h1>Initial</h1>
      <button onClick={getServices()}></button>
    </>
  )
}

export default App
