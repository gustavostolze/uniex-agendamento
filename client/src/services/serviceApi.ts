
import axios from 'axios';

export interface ServiceItem {
    id: number;
    name: string;
    description: string;
    price: number;
    timeMinutes: number;
}

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    withCredentials: true
});

export const getServicesByProfessional = async (professionalId: number): Promise<ServiceItem[]> => {
    const response = await api.get<ServiceItem[]>(`/services/professional/${professionalId}`);
    return response.data;
};