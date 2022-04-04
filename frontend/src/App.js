//
//import axios from "axios";
//import React, { useState } from "react";
//import styled from "styled-components";
//
//const InputStyle = styled.input`
//  width: calc(${(props) => props.width});
//  height: calc(${(props) => props.height});
//
//  border: none;
//  background-color: transparent;
//
//  color: ${(props) => props.color};
//  font-size: calc((${(props) => props.height}) / 5 * 2);
//  :focus {
//    outline-width: 0;
//  }
//
//  :focus::placeholder {
//    color: transparent;
//  }
//`;
//
//function Input({
//  width,
//  height,
//  placeholder,
//  setState,
//  name,
//  color,
//  fontSize,
//  state,
//}) {
//  const handleText = (e) => {
//    e.preventDefault();
//    if (setState) {
//      setState(e.target.value);
//    }
//  };
//  return (
//    <InputStyle
//      width={width}
//      height={height}
//      placeholder={placeholder}
//      onChange={handleText}
//      name={name}
//      color={color}
//      value={state}
//    />
//  );
//}
//
//export default function SignUp() {
//  const [email, setEmail] = useState("");
//  const [password, setPassword] = useState("");
//  const [username, setUsername] = useState("");
//  const [department, setDepartment] = useState("");
//  const [contact, setContact] = useState("");
//  const signUp = async () => {
//    const formData = new FormData();
//    formData.append("email", email);
//    formData.append("password", password);
//    formData.append("username", username);
//    formData.append("department", department);
//    formData.append("contact", contact);
//
//    try {
//      const response = await axios.post(
//        `http://13.124.176.247:8080/sign-up`,
//        formData,
//        {
//          headers: {
//            Accept: "application/json",
//            "Content-Type": "multipart/form-data",
//          },
//        }
//      );
//    } catch (error) {
//      // if (error.result.msg) {
//      //   // console.log(error);
//      // }
//    }
//  };
//  return (
//    <div>
//      <br />
//      <Input
//        width="300px"
//        height="30px"
//        setState={setEmail}
//        placeholder="email"
//      />
//      <br />
//      <Input
//        width="300px"
//        height="30px"
//        setState={setPassword}
//        placeholder="PassWord"
//      />
//      <br />
//      <Input
//        width="300px"
//        height="30px"
//        setState={setUsername}
//        placeholder="username"
//      />
//      <br />
//      <Input
//        width="300px"
//        height="30px"
//        setState={setDepartment}
//        placeholder="department"
//      />
//      <br />
//      <Input
//        width="300px"
//        height="30px"
//        setState={setContact}
//        placeholder="contact"
//      />
//      <button type="submit" onClick={signUp}>
//        회원가입
//      </button>
//    </div>
//  );
//}
///////////////////////////////////////////////////////////// ITEMS

import axios from "axios";
import React, { useState } from "react";
import styled from "styled-components";

const InputStyle = styled.input`
  width: calc(${(props) => props.width});
  height: calc(${(props) => props.height});

  border: none;
  background-color: transparent;

  color: ${(props) => props.color};
  font-size: calc((${(props) => props.height}) / 5 * 2);
  :focus {
    outline-width: 0;
  }

  :focus::placeholder {
    color: transparent;
  }
`;

function Input({
  width,
  height,
  placeholder,
  setState,
  name,
  color,
  fontSize,
  state,
}) {
  const handleText = (e) => {
    e.preventDefault();
    if (setState) {
      setState(e.target.value);
    }
  };
  return (
    <InputStyle
      width={width}
      height={height}
      placeholder={placeholder}
      onChange={handleText}
      name={name}
      color={color}
      value={state}
    />
  );
}

export default function SignUp() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [department, setDepartment] = useState("");
  const [contact, setContact] = useState("");
  const [type, setType] = useState("");
  const signUp = async () => {
    const formData = new FormData();
    formData.append("name", email);
    formData.append("thumbnail", password);
    formData.append("width", username);
    formData.append("height", department);
    formData.append("weight", contact);
    formData.append("type", type);
    axios.defaults.headers.common['Authorization'] = `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNjQ4ODgzODY2LCJleHAiOjE2NDg4ODU2NjZ9.5J7cVeLuBdUhh-OmefEWarZZVsa9Jos5IWqMSIc7DmY`
    try {
      const response = await axios.post(
        `http://13.124.176.247:8080/items`,
//        `http://127.0.0.1:8181/items`,
        formData,
        {
          headers: {

            Accept: "application/json",
            "Content-Type": "multipart/form-data",
          },
        }
      );
    } catch (error) {
      // if (error.result.msg) {
      //   // console.log(error);
      // }
    }
  };
  return (
    <div>
      <br />
      <Input
        width="300px"
        height="30px"
        setState={setEmail}
        placeholder="email"
      />
      <br />
      <Input
        width="300px"
        height="30px"
        setState={setPassword}
        placeholder="썸네일"
      />
      <br />
      <Input
        width="300px"
        height="30px"
        setState={setUsername}
        placeholder="너비"
      />
      <br />
      <Input
        width="300px"
        height="30px"
        setState={setDepartment}
        placeholder="높이"
      />
      <br />
      <Input
        width="300px"
        height="30px"
        setState={setContact}
        placeholder="무게"
      />
            <br />
            <Input
              width="300px"
              height="30px"
              setState={setType}
              placeholder="종류"
            />
      <button type="submit" onClick={signUp}>
        회원가입
      </button>
    </div>
  );
}